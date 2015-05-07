
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class Regelkreis {
	
	GenericRegler regler;
	StepResponse stepResponse = new StepResponse();

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
    	stepResponse.calc(regler.reglerTyp, regler.getResult(), verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum);
    }
    
    public double[] getXValues(){
    	return stepResponse.gettAxis();
    }
    
    public double[] getYValues(){
    	return stepResponse.getyAxis();
    }
    
    public void output(){
    	System.out.println("Regler Typ: "+regler.reglerTyp+" Resultat: "+Arrays.toString(regler.getResult()));
    }

}