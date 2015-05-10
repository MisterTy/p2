
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class Regelkreis {
	
	GenericRegler regler;
	StepResponse stepResponse = new StepResponse();
	boolean modified = false;
	DimensioningResult dimensioningResult;

    /**
     * 
     */
    public Regelkreis(int type, Complex[] utfStrecke, double[] kreisFrequenzSpektrum, double phasenrand, double verstarkungStrecke, double[] zeitkonstante){
    	switch (type){
		case Model.piRegler:
			regler = new PIRegler();
			regler.reglerTyp = Model.piRegler;
			regler.setValues(utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante);
			regler.compute();
			// Befehl Schrittantwort berechnen
			break;
		case Model.pidRegler:
			regler = new PIDRegler();
			regler.reglerTyp = Model.pidRegler;
			regler.setValues(utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante);
			regler.compute();
			// Befehl Schrittantwort berechnen
			break;
    	}
    	double[] params = regler.getResult().getParamArray();
    	stepResponse.calc(regler.getTyp(), params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum);
    	dimensioningResult = regler.getResult();
    }
    
    public void updateStepResponse(double[] params, double verstarkungStrecke, double[] zeitkonstante, double[]kreisFrequenzSpektrum){
    	stepResponse.calc(regler.getTyp(), params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum);
    	dimensioningResult.setValues(regler.getTyp(), params[0], params[1], params[2], params[3]);
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