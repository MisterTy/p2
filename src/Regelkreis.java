
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

/**
 * 
 */
public class Regelkreis {
	
	GenericRegler regler;
	StepResponse stepResponse = new StepResponse();
	DimensioningResult dimensioningResult;
	boolean modified = false;
	double overshoot;
	double oldOvershoot;
	
	double precision = 0.00001;

    /**
     * 
     */
    public Regelkreis(int type, Complex[] utfStrecke, double[] kreisFrequenzSpektrum, double gewunschtesUberschwingen, double verstarkungStrecke, double[] zeitkonstante, double kkfOffset){
    	overshoot = 101.0;
    	double phasenrand;
    	boolean klMerker = false;
    	boolean grMerker = false;
    	int counter = 0;
    	
    	if (gewunschtesUberschwingen >= 23.3){
    		phasenrand = 0.785;
    	} else if (gewunschtesUberschwingen >= 16.3){
    		phasenrand = 0.898;
    	} else if (gewunschtesUberschwingen >= 4.6){
    		phasenrand = 1.143;
    	} else {
    		phasenrand = 1.331;
    	}
    	double addFactor = phasenrand / 2;
    	
    	while (FastMath.abs(overshoot-oldOvershoot) > precision && counter < 100){
    		oldOvershoot = overshoot;
    		dimensionieren(type, utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante, kkfOffset);
        	overshoot = calcOvershoot();
        	
        	if (overshoot > gewunschtesUberschwingen){
        		if (klMerker){
        			addFactor /= 2;
        			klMerker = false;
        		}
        		phasenrand += addFactor;
        		grMerker = true;
        	} else if (overshoot < gewunschtesUberschwingen){
        		if (grMerker){
        			addFactor /= 2;
        			grMerker = false;
        		}
        		phasenrand -= addFactor;
        		klMerker = true;
        	}
        	counter++;
        	System.out.println("Current Overshoot: "+overshoot+" Wanted Overshoot: "+gewunschtesUberschwingen);
    	}
    	System.out.println("Optimisation took "+counter+" iterations.");
    	dimensioningResult = regler.getResult();
    }
    
    private void dimensionieren(int type, Complex[] utfStrecke, double[] kreisFrequenzSpektrum, double phasenrand, double verstarkungStrecke, double[] zeitkonstante, double kkfOffset){
    	double kkf = -Math.PI/2;
    	switch (type){
			case Model.piRegler:
				kkf = -Math.PI/2;
				regler = new PIRegler();
				regler.reglerTyp = Model.piRegler;
				break;
			case Model.pidRegler:
				kkf = -3*Math.PI/4;
				regler = new PIDRegler();
				regler.reglerTyp = Model.pidRegler;
				break;
	    }
    	kkf += kkfOffset;
    	System.out.println("kkf: "+kkf);
    	regler.setValues(utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante, kkf);
		regler.compute();
    	double[] params = regler.getResult().getParamArray();
    	stepResponse.calc(regler.getTyp(), params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum);
    }
    
    private double calcOvershoot() {
    	double[] yValues = getYValues();
    	double maximum = MathLibrary.findMax(yValues);
    	double endwert = yValues[yValues.length-1];
    	overshoot = ((100 / endwert) * maximum) - 100;
    	return overshoot;
    }
    
    public void updateStepResponse(double[] params, double verstarkungStrecke, double[] zeitkonstante, double[]kreisFrequenzSpektrum){
    	stepResponse.calc(regler.getTyp(), params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum);
    	dimensioningResult.setValues(regler.getTyp(), params[0], params[1], params[2], params[3]);
    }
    
    public int getTyp(){
    	return regler.getTyp();
    }
    
    public double[] getXValues(){
    	return stepResponse.gettAxis();
    }
    
    public double[] getYValues(){
    	return stepResponse.getyAxis();
    }
    
    public DimensioningResult getResult() {
    	return dimensioningResult;
    }
    
    public boolean getModified() {
    	return modified;
    }
    
    
    public void output(){
    	System.out.println("Regler Typ: "+regler.reglerTyp+" Resultat: "+Arrays.toString(regler.getResult().getParamArray()));
    }

}