package Model;

import java.util.*;

import org.apache.commons.math3.complex.Complex;

import Auxillary.DimensioningResult;

/**
 * Diese Klasse enthält das Grundgerüst für den PI und PID Regler 
 *
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
	double kkf;

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
    
    /**
     * Entfernt den Sprung um 2 Pi bei einer stetig fallenden funktion
     * 
     * @param liste Array mit einer Funktion mit Sprung
     * @return Funktion ohne Sprung
     */
    public double[] sprungEntfernen(double[] liste){
       	for (int y = 0; y < liste.length; y++) {
    		if(liste[y]>0){
    			for(int z=y;z < liste.length; z++)	liste[z]=-2*Math.PI+liste[z];
    			break;
    		}   		
		}
		return liste;
    	
    }

    /**
     * Werte zur berechnung der Regler uebergeben
     * 
     * @param utfStrecke Uebertragungsfunktion der Strecke
     * @param kreisFrequenzSpektrum 
     * @param phasenrand
     * @param verstarkungStrecke
     * @param zeitkonstante
     * @param knickKreisFrequenz
     */
    public void setValues(Complex[] utfStrecke, double[] kreisFrequenzSpektrum,
    		double phasenrand, double verstarkungStrecke, double[] zeitkonstante,
    		double knickKreisFrequenz) {
    	gs = utfStrecke;
    	w = kreisFrequenzSpektrum;
    	phir = phasenrand;
    	kS = verstarkungStrecke;
    	t = zeitkonstante;
    	kkf = knickKreisFrequenz;
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
    
    public void setKKF(double knickKreisFrequenz){
    	kkf = knickKreisFrequenz;
    }
    
    public int getTyp(){
    	return reglerTyp;
    }

}