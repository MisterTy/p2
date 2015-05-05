import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class Model extends Observable {
	final static public int piRegler = 1;
	final static public int pidRegler = 2;
	
	Strecke strecke;
	ArrayList<Regelkreis> regelKreisListe = new ArrayList<Regelkreis>();
	
	Complex[] utfStrecke;
	double[] kreisFrequenzSpektrum;
	double phasenrand = -1;
	double verstarkungStrecke = 1;
	double[] zeitkonstante;
	int anzahlPunkte;
	
    /**
     * 
     */
    public Model() {
    	
    }
    
    public void setAnzahlPunkte(int anzahl){
    	this.anzahlPunkte = anzahl;
    }
    
    public void setStrecke(double tu, double tg){
    	strecke = new Strecke(tu, tg);
    	zeitkonstante = strecke.getCoeffitients();
    	
    	// KreisW berechnen
    	double[] coeffitients = strecke.getCoeffitients();
    	double tMax = MathLibrary.findMax(coeffitients);
    	double tMin = MathLibrary.findMin(coeffitients);
    	kreisFrequenzSpektrum = MathLibrary.logspace(Math.log10(1/(tMax*10)), Math.log10(1/(tMin/10)), anzahlPunkte);
    	
    	strecke.calculateUtf(anzahlPunkte, kreisFrequenzSpektrum, verstarkungStrecke);
    	utfStrecke = strecke.getUtfStrecke();
    }
    
    public void setPhasenrand(double phir){
    	this.phasenrand = phir;
    }
    
    public void setVerstarkung(double verstarkung){
    	this.verstarkungStrecke = verstarkung;
    }
    
    public void addRegelkreis(int type){
    	switch (type){
    		case piRegler: case pidRegler:
    			Regelkreis regelkreis = new Regelkreis(type, utfStrecke, kreisFrequenzSpektrum, phasenrand, verstarkungStrecke, zeitkonstante);
    			regelKreisListe.add(regelkreis);
    			break;
    	}
    	
    }
    
    public void output(){
    	for (Regelkreis regelkreis : regelKreisListe){
    		regelkreis.output();
    		
    	}
    }
    



    /**
     * 
     */
    public void notifyObservers() {
        // TODO implement here
    }

}