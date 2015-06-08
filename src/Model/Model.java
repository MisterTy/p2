package Model;
import java.util.ArrayList;
import java.util.Observable;

import org.apache.commons.math3.complex.Complex;

import Auxillary.MathLibrary;
import Auxillary.Notification;

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
	

    public Model() {
    	
    }
    
    public void setAnzahlPunkte(int anzahl){
    	this.anzahlPunkte = anzahl;
    }
    
    /**
     * Berechnet die Strecke und deren Uebertragungsfunktion
     * @param tu Tu
     * @param tg Tg
     */
    public void setStrecke(double tu, double tg){
    	strecke = new Strecke(tu, tg);
    	zeitkonstante = strecke.getCoeffitients();
    	
    	// KreisW berechnen
    	double[] coeffitients = strecke.getCoeffitients();
    	double tMax = MathLibrary.findMax(coeffitients);
    	double tMin = MathLibrary.findMin(coeffitients);
    	kreisFrequenzSpektrum = MathLibrary.logspace(Math.log10(1/(tMax*10)), Math.log10(1/(tMin/10)), anzahlPunkte);
    	
    	strecke.calculateUtf(anzahlPunkte, kreisFrequenzSpektrum, verstarkungStrecke);
    }
    
    public void setUberschwingen(double uberschwingenProzent){
    	this.uberschwingen = uberschwingenProzent;
    }
    
    public void setVerstarkung(double verstarkung){
    	this.verstarkungStrecke = verstarkung;
    }
    
    /**
     * Erstellt einen neuen Regelkreis
     * @param type Regler-Typ
     */
    public void addRegelkreis(int type){
    	switch (type){
    		case piRegler: case pidRegler:
    			Regelkreis regelkreis = new Regelkreis(type, strecke, kreisFrequenzSpektrum, uberschwingen, verstarkungStrecke, zeitkonstante, 0);
    			regelKreisListe.add(regelkreis);
    			Notification note = new Notification(Notification.newRegelkreis);
    			note.setRegelkreis(regelkreis);
    			note.setDimensioningResult(regelkreis.getResult());
    			setChanged();
    			notifyObservers(note);
    			break;
    	}
    }
    
    /**
     * Aendert den ausgewaehlten Regelkreis
     * @param kkfOffset
     * @param regelkreisIndex
     */
    public void updateRegelkreis(double kkfOffset, int regelkreisIndex){
    	int n = strecke.getN();
    	double modifier = n / 25.0;
    	modifier *= kkfOffset;
    	Regelkreis oldRegelkreis = regelKreisListe.get(regelkreisIndex);
    	Regelkreis newRegelkreis = new Regelkreis(oldRegelkreis.getTyp(), strecke, kreisFrequenzSpektrum, uberschwingen, verstarkungStrecke, zeitkonstante, modifier);
    	newRegelkreis.setKkfRaw(kkfOffset);
    	removeRegelkreis(regelkreisIndex);
    	regelKreisListe.add(regelkreisIndex, newRegelkreis);
    	Notification note = new Notification(Notification.updatedRegelkreis);
    	note.setRegelkreis(newRegelkreis);
    	note.setDimensioningResult(newRegelkreis.getResult());
    	setChanged();
    	notifyObservers(note);
    }
    
    /**
     * Updatet die Schrittantwort und Berechnet sie neu
     * @param regelkreis Regelkreis
     * @param params Parameterliste
     */
    public void updateStepResponse(int regelkreis, double[] params){
    	regelKreisListe.get(regelkreis).updateStepResponse(params, verstarkungStrecke, zeitkonstante, kreisFrequenzSpektrum);
    	Notification note = new Notification(Notification.updatedStepResponse);
    	note.setRegelkreis(regelKreisListe.get(regelkreis));
    	note.setDimensioningResult(note.getRegelkreis().getResult());
    	setChanged();
    	notifyObservers(note);
    }
    
    /**
     * Ueberprüft ob eine Neuberechnung notwendig ist.
     * @param regelkreis 
     * @param kkfRaw
     * @return
     */
    public boolean updateNecessary(int regelkreis, double kkfRaw){
    	if (regelKreisListe.get(regelkreis).getKkfRaw() != kkfRaw){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Loescht den übergebenen Regelkreis.
     * @param regelkreis Regelkreis
     */
    public void removeRegelkreis(int regelkreis){
    	regelKreisListe.remove(regelkreis);
    }
    
    /**
     * Löscht alle Regelkreise
     */
    public void deleteAllRegelkreise(){
    	regelKreisListe.clear();
    	strecke = null;
    }
    
    /**
     * Gibt die Anzahl der Regelkreise zurueck
     */
    public int getAnzRegelkreise(){
    	return regelKreisListe.size();
    }
    
    public double[] getXValues(){
		return regelKreisListe.get(0).getXValues();    	
    }
    
    public double[] getYValues(){
		return regelKreisListe.get(0).getYValues();    	
    }
}