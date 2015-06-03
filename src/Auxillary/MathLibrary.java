package Auxillary;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

/**
 * Bibliothek mit Berechnugsfunktionen (Die Meisten sind equivalent zu Matlabfunktionen)
 */
public class MathLibrary {
    
    /**
     * Methode sucht index eines Punktes (Phi) aus einem Double Array (phase).
     * Gibt ein Integer Array zurueck mit 2 Indexwerten, 
     * index unterhalb (0) und oberhalb (1) des gesuchten Punktes.
     * @param phase Phasengang
     * @param phi Winkel
     * @return [unterer Index, oberer Index]
     */
    public static int[] int_ver(double[] phase,double phi){
    	int right = phase.length;
    	int left = 0;
    	int ind=0;
    	
    	while(right-left!=1){
    		ind=(right+left)/2;
    		if(phase[ind]>phi)	left=(right+left)/2;    			
    		else right=(right+left)/2;    		
    	}
    	
       	return new int[] {left,right};
    	
    }
    
    /**
     * Gibt eine linear unterteilte Werteliste als Double Array zurueck, mit n Punkten
     * - Ersetzt Matlabfunktion linspace()
     * @param start
     * @param end
     * @param n Anzahl Werte
     * @return lineare Werteliste
     */
    public static double[] linspace(double start,double end,int n){
    	double space[] =new double[n];    	
    	for(int i = 0;i<n;i++) space[i]=start+i*(end-start)/(n-1);
   		return space;   
    }
    
	/**
	 * Gibt eine linear unterteilte Werteliste als Double Array zurueck mit 100 Punkten
	 * - Ersetzt Matlabfunktion linspace()
	 * @param start
	 * @param end
	 * @return lineare Werteliste
	 */
    public static double[] linspace(double start,double end){
    	double space[] =new double[100];    	
    	for(double i = 0;i<100;i++) space[(int)i]=start+i*(end-start)/(99);
		return space;
    	
    }
    
    /**
     * Gibt eine Logarithmisch unterteile Werteliste von 10^start bis und mit 10^ende mit n Anzahl Punkten zurueck.
     * - Ersetzt Matlabfunktion logspace()
     * @param start
     * @param end
     * @param n Anzahl Werte
     * @return Werteliste
     */
    public static double[] logspace(double start,double end,int n){
    	double[] space=linspace(start,end,n);
    	for (int i = 0; i < n; i++) space[i]=Math.pow(10, space[i]);
		return space;    	
    }
    
    /**
     * Gibt eine Logarithmisch unterteile Werteliste von 10^start bis und mit10^ende mit 100 Punkten zurueck
     * - Ersetzt Matlabfunktion logspace
     * @param start
     * @param end
     * @return Werteliste
     */
    public static double[] logspace(double start,double end){
    	double[] space=linspace(start,end,100);
    	for (int i = 0; i < 100; i++) space[i]=Math.pow(10, space[i]);
		return space;
    	
    }
    /**
     * Sucht den groessten Wert aus einem Array und gibt diesen zurueck
     * - Ersetzt Matlabfunktion max()
     * 
     * @param werte Liste aus der der maximalwert gesucht wird.
     * @return Maximalwert
     */
	public static double findMax(double[] werte){
		double maximalwert=werte[0];
		for (int i = 0; i < werte.length; i++) if(werte[i]>maximalwert)maximalwert=werte[i];
		return maximalwert;
	}
	/**
	 * Sucht den kleinsten Wert aus einem Array und gibt diesen zurueck
	 * - Ersetzt Matlabfunktion min()
	 * 
	 * @param werte Werteliste aus der der Minimalwert gesucht wird.
	 * @return Minimalwert
	 */
	public static double findMin(double[] werte){
		double maximalwert=werte[0];
		for (int i = 0; i < werte.length; i++) if(werte[i]<maximalwert)maximalwert=werte[i];
		return maximalwert;
	}
	
	/**
	 * Berechnet den Frequenzgang aus Zaehler und Nennerpolynom und gibt diesen zurueck
	 *
	 * - Ersetzt Matlabfunktion freqs()
	 * 
	 * @param b Zählerpolynom
	 * @param a Nennerpolynom
	 * @param w Frequenzenliste
	 * @return Frequenzgang
	 */
	public static final Complex[] freqs(double[] b, double[] a, double[] w) {
		Complex[] res = new Complex[w.length];

		for (int k = 0; k < res.length; k++) {
			Complex jw = new Complex(0,w[k]);
			
			if (jw.equals(Complex.ZERO)){		// f??r omega==0 
				res[k] = new Complex(b[b.length-1] / a[a.length-1], 0);
			} else {
				Complex zaehler = new Complex(0, 0);
				for (int i = 0; i < b.length; i++) {
					zaehler = zaehler.add(jw.pow(b.length - i - 1).multiply(b[i]));
				}

				Complex nenner = new Complex(0, 0);
				for (int i = 0; i < a.length; i++) {
					nenner = nenner.add(jw.pow(a.length - i - 1).multiply(a[i]));
				}
				res[k] = zaehler.divide(nenner);
			}
		}
		return res;
	}
	
	/**
	 * Erzeugt ein eindimesionales Array, jeweils gefuellt mit dem Wert 1.
	 * - Ersetzt Matlabfunktion ones()
	 * 
	 * @param anzSpalten, Anzahl Elemente des erzeugten Arrays
	 * @return EinerArray 
	 */
	public static double[] ones(int anzSpalten){
		double[] result = new double[anzSpalten];
		for (int i=0; i<anzSpalten; i++){
			result[i] = 1;
		}
		return result;
	}
	
	//TODO Nicht mehr benoetigt?
	
/*	public static Complex[] prepareForFFT(Complex[] fftVector){
		int oldLength = fftVector.length;
		int newLength = (int) Math.pow(2, FastMath.ceil(FastMath.log(2.0, oldLength)));
		Complex[] result = new Complex[newLength];
		for (int i=0; i<newLength; i++){
			if (i<fftVector.length){
				result[i] = fftVector[i];
			} else {
				result[i] = Complex.ZERO;
			}
		}
		return result;
	}*/
	
	/**
	 * Diese Funktion rundet auf die nächste 2er Potenz auf.
	 * 
	 * @param oldValue Eingabewert
	 * @return auf naechste 2er Potenz gerundeter Wert
	 */
	public static int makePowOf2(int oldValue){
		return (int) Math.pow(2, FastMath.ceil(FastMath.log(2.0, oldValue)));
	}
	
	/**
	 * Wandelt Zahl in Engineering Format um.
	 * @param input
	 * @return Formatierte Nummer
	 */
	public static String scientificFormat(double input){
		final int upperLimit = 100;
		final double lowerLimit = 0.001;
		final String numberFormat;
		if (input > lowerLimit && input < upperLimit){
			numberFormat = "%1$,.3f";
		} else {
			numberFormat = "%6.3e";
		}
		return String.format(numberFormat, input);
	}

	
}