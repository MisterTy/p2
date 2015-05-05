
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
			break;
		case Model.pidRegler:
			PIDRegler regler = new PIDRegler();
			regler.reglerTyp = Model.pidRegler;
			regler.setValues(utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante);
			regler.compute();
			break;
    	}
    	
    }
    
    public void output(){
    	System.out.print("Regler Typ: "+regler.reglerTyp + "Resultat: "+regler.getResult());
    }

}