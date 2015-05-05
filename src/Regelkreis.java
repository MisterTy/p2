
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class Regelkreis {
	
	GenericRegler regler;

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
			break;
		case Model.pidRegler:
			regler = new PIDRegler();
			regler.reglerTyp = Model.pidRegler;
			regler.setValues(utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante);
			regler.compute();
			break;
    	}
    	
    }
    
    public void output(){
    	System.out.println("Regler Typ: "+regler.reglerTyp+" Resultat: "+Arrays.toString(regler.getResult()));
    }

}