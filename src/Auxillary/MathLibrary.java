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
	
private static final double nan = Double.parseDouble("NaN");
	
	public static final double[][] tuTg = {{nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan}, //Batman
		{0,0.015703,0.027157,0.03632,0.043925,0.050381,0.055946,0.060797,0.065063,0.068841,0.072205,0.075216,0.077921,0.080359,0.082563,0.084561,0.086375,0.088025,0.089528,0.090898,0.09215,0.093293,0.094337,0.095292,0.096165,0.096963,0.097693,0.098359,0.098967,0.099521,0.10002,0.10048,0.1009,0.10128,0.10161,0.10192,0.10219,0.10244,0.10265,0.10284,0.10301,0.10315,0.10327,0.10338,0.10346,0.10353,0.10358,0.10361,0.10363,0.10364},
		{0,0.016087,0.028607,0.039425,0.049205,0.058292,0.066891,0.075128,0.08308,0.090795,0.098298,0.1056,0.11269,0.11957,0.12622,0.13263,0.1388,0.14471,0.15036,0.15574,0.16085,0.16569,0.17026,0.17457,0.17862,0.18242,0.18597,0.18928,0.19236,0.19522,0.19786,0.2003,0.20254,0.20459,0.20646,0.20816,0.20969,0.21107,0.2123,0.21339,0.21435,0.21518,0.21589,0.21649,0.21698,0.21736,0.21766,0.21786,0.21798,0.21802},
		{0,0.016095,0.028666,0.039616,0.049639,0.05911,0.068256,0.077224,0.086112,0.094981,0.10387,0.11279,0.12175,0.13074,0.13976,0.14877,0.15776,0.16672,0.1756,0.18439,0.19307,0.2016,0.20996,0.21812,0.22605,0.23374,0.24115,0.24827,0.25507,0.26154,0.26768,0.27346,0.27888,0.28394,0.28863,0.29295,0.29692,0.30052,0.30378,0.30669,0.30927,0.31152,0.31347,0.31511,0.31646,0.31754,0.31835,0.31892,0.31925,0.31936},
		{0,0.016095,0.028669,0.039628,0.049675,0.059194,0.068423,0.077525,0.086609,0.095754,0.10501,0.11442,0.124,0.13376,0.1437,0.15383,0.16413,0.1746,0.18522,0.19599,0.20687,0.21784,0.22887,0.23993,0.25099,0.26199,0.27289,0.28365,0.29422,0.30454,0.31457,0.32426,0.33356,0.34243,0.35084,0.35874,0.36611,0.37293,0.37917,0.38483,0.3899,0.39439,0.39828,0.4016,0.40435,0.40656,0.40823,0.4094,0.41008,0.4103},
		{0,0.016095,0.028669,0.039629,0.049678,0.059203,0.068444,0.077568,0.08669,0.095896,0.10525,0.11479,0.12455,0.13456,0.14483,0.15538,0.16623,0.17736,0.18881,0.20055,0.2126,0.22495,0.23758,0.25047,0.2636,0.27694,0.29044,0.30405,0.31772,0.33138,0.34496,0.35839,0.37157,0.38444,0.39689,0.40884,0.42021,0.43093,0.44092,0.45012,0.45848,0.46597,0.47255,0.47821,0.48294,0.48676,0.48968,0.49172,0.49291,0.4933},
		{0,0.016095,0.028669,0.039629,0.049678,0.059203,0.068446,0.077574,0.086704,0.095922,0.10529,0.11487,0.12469,0.13477,0.14516,0.15586,0.16691,0.17833,0.19013,0.20233,0.21496,0.22802,0.24153,0.25548,0.26988,0.28471,0.29994,0.31556,0.33153,0.34777,0.36423,0.38082,0.39745,0.41401,0.43037,0.44641,0.46197,0.47693,0.49113,0.50444,0.51673,0.5279,0.53785,0.54651,0.55382,0.55977,0.56433,0.56754,0.56943,0.57004},
		{0,0.016095,0.028669,0.039629,0.049678,0.059204,0.068446,0.077575,0.086706,0.095927,0.1053,0.11489,0.12472,0.13483,0.14525,0.15601,0.16714,0.17866,0.19061,0.20302,0.21593,0.22935,0.24331,0.25785,0.27297,0.2887,0.30505,0.322,0.33954,0.35765,0.37628,0.39536,0.41482,0.43454,0.4544,0.47422,0.49384,0.51306,0.53165,0.54939,0.56607,0.58146,0.59538,0.60764,0.61812,0.62671,0.63336,0.63806,0.64083,0.64173}};
	
	public static final double[][] tTg = {{nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan,nan},
		{0.99925,0.92211,0.87274,0.83346,0.80034,0.77154,0.746,0.72302,0.70213,0.68298,0.66531,0.64892,0.63363,0.61931,0.60586,0.59318,0.5812,0.56985,0.55907,0.54881,0.53903,0.52968,0.52075,0.51219,0.50398,0.4961,0.48852,0.48122,0.47419,0.4674,0.46086,0.45453,0.44841,0.44249,0.43676,0.4312,0.42581,0.42058,0.4155,0.41057,0.40577,0.40111,0.39657,0.39215,0.38784,0.38365,0.37956,0.37557,0.37168,0.36788},
		{0.99925,0.92211,0.87271,0.83336,0.80011,0.7711,0.74524,0.72184,0.70038,0.68051,0.66195,0.64446,0.62788,0.61206,0.5969,0.58231,0.56822,0.55459,0.54138,0.52855,0.51609,0.50397,0.49219,0.48072,0.46957,0.45871,0.44814,0.43786,0.42786,0.41813,0.40865,0.39944,0.39048,0.38176,0.37328,0.36503,0.35701,0.34921,0.34162,0.33424,0.32707,0.32009,0.3133,0.30669,0.30027,0.29402,0.28794,0.28202,0.27627,0.27067},
		{0.99925,0.92211,0.87271,0.83336,0.80011,0.7711,0.74523,0.72182,0.70034,0.68044,0.66183,0.64427,0.62759,0.61164,0.5963,0.58148,0.5671,0.55312,0.53947,0.52612,0.51305,0.50023,0.48763,0.47525,0.46306,0.45107,0.43927,0.42766,0.41622,0.40498,0.39392,0.38306,0.37239,0.36192,0.35166,0.34161,0.33177,0.32214,0.31274,0.30356,0.2946,0.28587,0.27736,0.26908,0.26102,0.25319,0.24558,0.23818,0.23101,0.22404},
		{0.99925,0.92211,0.87271,0.83336,0.80011,0.7711,0.74523,0.72181,0.70034,0.68044,0.66182,0.64426,0.62757,0.61161,0.59625,0.5814,0.56699,0.55295,0.53923,0.52579,0.51259,0.4996,0.48679,0.47415,0.46165,0.44928,0.43702,0.42488,0.41283,0.40088,0.38904,0.37729,0.36565,0.35414,0.34274,0.33149,0.32039,0.30945,0.29869,0.28813,0.27776,0.26762,0.2577,0.24802,0.23859,0.22942,0.22051,0.21186,0.20348,0.19537},
		{0.99925,0.92211,0.87271,0.83336,0.80011,0.7711,0.74523,0.72181,0.70034,0.68044,0.66182,0.64426,0.62757,0.61161,0.59625,0.5814,0.56698,0.55293,0.5392,0.52574,0.51251,0.49949,0.48663,0.47392,0.46133,0.44883,0.43642,0.42408,0.41178,0.39953,0.38732,0.37513,0.36298,0.35086,0.33879,0.32677,0.31482,0.30295,0.29119,0.27957,0.26809,0.25679,0.2457,0.23483,0.22421,0.21386,0.20379,0.19403,0.18459,0.17547},
		{0.99925,0.92211,0.87271,0.83336,0.80011,0.7711,0.74523,0.72181,0.70034,0.68044,0.66182,0.64426,0.62757,0.61161,0.59625,0.5814,0.56698,0.55293,0.53919,0.52573,0.5125,0.49947,0.4866,0.47387,0.46125,0.44872,0.43626,0.42384,0.41145,0.39907,0.38669,0.3743,0.36188,0.34944,0.33697,0.32448,0.31198,0.29948,0.287,0.27457,0.26223,0.24999,0.23791,0.22602,0.21435,0.20294,0.19184,0.18107,0.17065,0.16062},
		{0.99925,0.92211,0.87271,0.83336,0.80011,0.7711,0.74523,0.72181,0.70034,0.68044,0.66182,0.64426,0.62757,0.61161,0.59625,0.5814,0.56698,0.55293,0.53919,0.52573,0.5125,0.49946,0.48659,0.47386,0.46123,0.44869,0.43621,0.42377,0.41135,0.39892,0.38647,0.37398,0.36143,0.34882,0.33613,0.32335,0.3105,0.29758,0.28459,0.27158,0.25855,0.24557,0.23266,0.21988,0.20729,0.19494,0.18289,0.17118,0.15988,0.149}};

	
}