import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

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
	double uberschwingen = -1;
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
    
    public void setUberschwingen(double uberschwingenProzent){
    	this.uberschwingen = uberschwingenProzent;
    }
    
    public void setVerstarkung(double verstarkung){
    	this.verstarkungStrecke = verstarkung;
    }
    
    public void addRegelkreis(int type){
    	switch (type){
    		case piRegler: case pidRegler:
    			Regelkreis regelkreis = new Regelkreis(type, utfStrecke, kreisFrequenzSpektrum, uberschwingen, verstarkungStrecke, zeitkonstante);
    			regelKreisListe.add(regelkreis);
    			setChanged();
    			notifyObservers(regelkreis.getResult());
    			break;
    	}
    }
    
    public void updateStepResponse(int regelkreis, double[] params){
    	regelKreisListe.get(regelkreis).updateStepResponse(params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum);
    	//setChanged();
    	//notifyObservers(regelKreisListe.get(regelkreis).getResult());
    }
    
    public void removeRegelkreis(){
    	regelKreisListe.remove(0);
    }
    
    public void output(){
    	for (Regelkreis regelkreis : regelKreisListe){
    		regelkreis.output();
    	}
    }
    
    public double[] getXValues(){
		return regelKreisListe.get(0).getXValues();    	
    }
    
    public double[] getYValues(){
		return regelKreisListe.get(0).getYValues();    	
    }
}