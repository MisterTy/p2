
import java.util.*;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class GenericRegler {
	public int reglerTyp = 0;
	DimensioningResult result = new DimensioningResult();
	
	Complex[] gs;
	double[] w;
	double phir;
	double kS;
	double[] t;

    /**
     * 
     */
    public GenericRegler() {
    }
    
    public DimensioningResult getResult(){
    	return result;
    }
    
    public void compute(){
    	
    }
    
    
    public double[] sprungEntfernen(double[] liste){
       	for (int y = 0; y < liste.length; y++) {
    		if(liste[y]>0){
    			for(int z=y;z < liste.length; z++)	liste[z]=-2*Math.PI+liste[z];
    			break;
    		}   		
		}
		return liste;
    	
    }

    public void setValues(Complex[] utfStrecke, double[] kreisFrequenzSpektrum, double phasenrand, double verstarkungStrecke, double[] zeitkonstante) {
    	gs = utfStrecke;
    	w = kreisFrequenzSpektrum;
    	phir = phasenrand;
    	kS = verstarkungStrecke;
    	t = zeitkonstante;
    }
    
    public void setGs(Complex[] utfStrecke){
    	gs = utfStrecke;
    }
    
    public void setW(double[] kreisFrequenzSpektrum){
    	w = kreisFrequenzSpektrum;
    }
    
    public void setPhir(double phasenrand){
    	phir = phasenrand;
    }
    
    public void setKs(double verstarkungStrecke){
    	kS = verstarkungStrecke;
    }
    
    public void setT(double[] zeitkonstante){
    	t = zeitkonstante;
    }
    
    public int getTyp(){
    	return reglerTyp;
    }

}