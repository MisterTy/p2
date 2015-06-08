package Auxillary;

public class SaniCalculator {
	
	int n = -1;
	double[] t;
	
	private double[][] tuTg = new double[8][50];
	private double[][] tTg = new double[8][50];

	/**
	 * Berechnet die Strecke anhand der Sani Methode
	 * @param tu
	 * @param tg
	 */
	public void saniCalculation(double tu, double tg) {
		tuTg = MathLibrary.tuTg;
		tTg = MathLibrary.tTg;
		
        double v = calculateV(tu, tg);
        n = calculateN(v);
        t = splineInterpolation(tg, n, v);
	}
	
	public int getN(){
		return n;
	}
	
	public double[] getT(){
		return t;
	}
	
	/**
	 * Berechnet aus den Werten Tu und Tg das Verhältnis v
	 * @param tu
	 * @param tg
	 * @return Verhältnis
	 */
	private double calculateV(double tu, double tg){
		return tu / tg;
	}
	
	/**
	 * Berechnet aus dem Verhältnis V den Grad der Strecke. 
	 * @param v Verhältnis aus Tu und Tg
	 * @return Grad der Strecke
	 */
	private int calculateN(double v) {
		int n = -1;
		if (v <= 0.103638) {
			n = 2;
		}
		else if (v <= 0.218017) {
			n = 3;
		}
		else if (v <= 0.319357) {
			n = 4;
		}
		else if (v <= 0.410303) {
			n = 5;
		}
		else if (v <= 0.4933) {
			n = 6;
		}
		else if (v <= 0.5700) {
			n = 7;
		}
		else if (v <= 0.64173) {
			n = 8;
		}
		else {
			n = 10;
		}
		return n;
	}
	
	/**
	 *  Führt eine Cubicspline Interpolation durch.
	 * @param tg
	 * @param n Anzahl Punkte
	 * @param v
	 * @return
	 */
	private double[] splineInterpolation(double tg, int n, double v) {
		
		double[] li = MathLibrary.linspace(0, 1, 50);
		double[] linearTerms = new double[50];
		double[] quadraticTerms = new double[50];
		double[] cubicTerms = new double[50];
		SplineNAK.cubic_nak(50, tuTg[n-1], li, linearTerms, quadraticTerms, cubicTerms);
		double r = SplineNAK.spline_eval(50, tuTg[n-1], li, linearTerms, quadraticTerms, cubicTerms, v);
		
		SplineNAK.cubic_nak(50, li, tTg[n-1], linearTerms, quadraticTerms, cubicTerms);
		double w = SplineNAK.spline_eval(50, li, tTg[n-1], linearTerms, quadraticTerms, cubicTerms, r);
		
		double[] result = new double[n];
		result[n-1] = w*tg;
		
		for (int i = n-2; i >= 0; i--) {
			result[i] = result[n-1]*Math.pow(r, n-i-1);
		}
		return result;
		
	}

}
