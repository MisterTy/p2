import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class Strecke {
	double tu = -1;
	double tg = -1;
	double[] coeffitients;
	int n;
	Complex[] utfStrecke;

    /**
     * 
     */
    public Strecke(double tu, double tg) {
    	this.tu = tu;
    	this.tg = tg;
    	SaniCalculator saniCalculator = new SaniCalculator();
    	saniCalculator.saniCalculation(tu, tg);
    	coeffitients = saniCalculator.getT();
    	n = saniCalculator.getN();
    }
    
    public void calculateUtf(int anzahlPunkte, double[] w, double ks){
    	utfStrecke = new Complex[anzahlPunkte];
		
		for (int i = 0; i < utfStrecke.length; i++) utfStrecke[i]=new Complex(1, 0);
		for (int i = 0; i < n; i++) {
    		for (int j = 0; j < utfStrecke.length; j++) {
    			utfStrecke[j] = utfStrecke[j].multiply(((Complex.I.multiply(w[j]).multiply(coeffitients[i]).add(1))).reciprocal());
			}
			
		}
		for (int i = 0; i < utfStrecke.length; i++){
			utfStrecke[i]=utfStrecke[i].multiply(ks);		//Gs Liste komp.
		}
    	
    }
    
    public Complex[] getUtfStrecke(){
    	return utfStrecke;
    }
    
    public int getN(){
    	return n;
    }
    
    public double[] getCoeffitients(){
    	return coeffitients;
    }


}