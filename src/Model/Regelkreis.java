package Model;

import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

import Auxillary.DimensioningResult;
import Auxillary.MathLibrary;

/**
 * 
 */
public class Regelkreis {
	private GenericRegler regler;
	private StepResponse stepResponse = new StepResponse();
	private DimensioningResult dimensioningResult;
	private boolean modified = false;
	private double wantedOvershoot;
	private double overshoot;
	private double oldOvershoot;
	private double precision = 0.00001;
	private double kkfRaw = 0;
	private Strecke strecke;
	private Complex[] utfStrecke;

    /**
     * 
     */
    public Regelkreis(int type, Strecke strecke, double[] kreisFrequenzSpektrum, double gewunschtesUberschwingen,
    		double verstarkungStrecke, double[] zeitkonstante, double kkfOffset){
    	
    	this.strecke = strecke;
    	utfStrecke = this.strecke.getUtfStrecke();
    	wantedOvershoot = gewunschtesUberschwingen;
    	overshoot = 101.0;
    	double phasenrand;
    	boolean klMerker = false;
    	boolean grMerker = false;
    	int counter = 0;
    	
    	if (wantedOvershoot >= 23.3){
    		phasenrand = 0.785;
    	} else if (wantedOvershoot >= 16.3){
    		phasenrand = 0.898;
    	} else if (wantedOvershoot >= 4.6){
    		phasenrand = 1.143;
    	} else {
    		phasenrand = 1.331;
    	}
    	double addFactor = phasenrand / 2;
    	
    	while (FastMath.abs(overshoot-oldOvershoot) > precision && counter < 100){
    		oldOvershoot = overshoot;
    		dimensionieren(type, utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante, kkfOffset, counter==0);
        	overshoot = calcOvershoot();
        	
        	if (overshoot > wantedOvershoot){
        		if (klMerker){
        			addFactor /= 2;
        			klMerker = false;
        		}
        		phasenrand += addFactor;
        		grMerker = true;
        	} else if (overshoot < wantedOvershoot){
        		if (grMerker){
        			addFactor /= 2;
        			grMerker = false;
        		}
        		phasenrand -= addFactor;
        		klMerker = true;
        	}
        	counter++;
    	}
    	dimensioningResult = regler.getResult();
    }
    
    private void dimensionieren(int type, Complex[] utfStrecke, double[] kreisFrequenzSpektrum, double phasenrand,
    		double verstarkungStrecke, double[] zeitkonstante, double kkfOffset, boolean initial){
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
    	regler.setValues(utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante, kkf);
		regler.compute();
    	double[] params = regler.getResult().getParamArray();
    	stepResponse.calc(regler.getTyp(), params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum, initial);
    }
    
    private double calcOvershoot() {
    	double[] yValues = getYValues();
    	double maximum = MathLibrary.findMax(yValues);
    	double endwert = 1;//yValues[yValues.length-1];
    	overshoot = (maximum-1)*100;
    	return overshoot;
    }
    
    public void updateStepResponse(double[] params, double verstarkungStrecke, double[] zeitkonstante, double[]kreisFrequenzSpektrum){
    	stepResponse.calc(regler.getTyp(), params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum, true);
    	dimensioningResult.setValues(regler.getTyp(), params[0], params[1], params[2], params[3]);
    	calcOvershoot();
    }
    
    public void setKkfRaw(double kkfRaw){
    	this.kkfRaw = kkfRaw;
    }
    public double getKkfRaw(){
    	return kkfRaw;
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
    
    public DimensioningResult getResult(){
    	return dimensioningResult;
    }
    
    public boolean getModified(){
    	return modified;
    }
    
    public Strecke getStrecke(){
    	return strecke;
    }
    
    public double getWantedOvershoot(){
    	return wantedOvershoot;
    }
    
    public double getOvershoot(){
    	return overshoot;
    }
    
    
    public void output(){
    	System.out.println("Regler Typ: "+regler.reglerTyp+" Resultat: "+Arrays.toString(regler.getResult().getParamArray()));
    }
}