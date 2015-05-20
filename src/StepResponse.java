import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;


public class StepResponse {
	
	//a = Z�hlerpolynom/b=Reglerpolynom/S= Strecke/R=Regler/T=Terme
	private double [] a;
	private double [] b;
	private double [] yAxis;
	private double [] tAxis;
	private int n;
	private double ws;
	private double [] temp;
	
	public StepResponse(){
	}
	
	
	public void calc(int type, double[] reglerParameter, double streckenbeiwert, double [] zeitKonstantenStrecke, double[] kreisFrequenzspektrum){
		double [][] aST;
		double  bS[] = new double[1];	
		double [] aS;	
		double [] aR;
		double [] bR;
		final int accelerate = 4;
		
		Complex[] gR = new Complex[kreisFrequenzspektrum.length / accelerate];
		Complex[] gS = new Complex[kreisFrequenzspektrum.length / accelerate];
		double[] aOdb = new double[kreisFrequenzspektrum.length / accelerate];
		Complex tmp;
		Complex[] pidTerm = new Complex[kreisFrequenzspektrum.length / accelerate];
		
		switch (type) {
		case Model.piRegler:
			for (int i = 0; i < kreisFrequenzspektrum.length; i+=accelerate){
				pidTerm[i/accelerate] = new Complex(0,0);
			}
			break;
			
		case Model.pidRegler:
			for (int i = 0; i < kreisFrequenzspektrum.length; i+=accelerate){
				pidTerm[i/accelerate] = new Complex(0, kreisFrequenzspektrum[i] * reglerParameter[2]).divide(new Complex(1, kreisFrequenzspektrum[i] * reglerParameter[3]));
			}
			break;
		}
		
		for (int i = 0; i < kreisFrequenzspektrum.length; i+=accelerate){
			gR[i/accelerate] = new Complex(1, -1 / (kreisFrequenzspektrum[i] * reglerParameter[1])).add(pidTerm[i/accelerate]).multiply(reglerParameter[0]);
			tmp = new Complex(1, 0);
			for (int j = 0; j < zeitKonstantenStrecke.length; j++){
				tmp = tmp.multiply(new Complex(1, kreisFrequenzspektrum[i] * zeitKonstantenStrecke[j]).reciprocal());
			}
			gS[i/accelerate] = tmp.multiply(streckenbeiwert);
			aOdb[i/accelerate] = gR[i/accelerate].multiply(gS[i/accelerate]).abs();
			aOdb[i/accelerate] = FastMath.log10(aOdb[i/accelerate]) * 20;
		}
		
		
		
		
		int [] indeces = MathLibrary.int_ver(aOdb, -30);
		int factor = -10 * zeitKonstantenStrecke.length + 88;
		ws = factor * (kreisFrequenzspektrum[indeces[0]] + kreisFrequenzspektrum[indeces[1]]) / 2;
		n = (int)FastMath.pow(2, FastMath.ceil(FastMath.log(2, indeces[1])));
		
		aST = new double[zeitKonstantenStrecke.length][2]; 			//Speicher die Terme (1+sT1)(1+sT2)(1+sT3).....(1+sTn)
		temp = new double[1];
		temp[0] = 1;
		//System.out.println("tmp: "+Arrays.toString(tmp));
		for (int i = 0; i < zeitKonstantenStrecke.length; i++) {
			aST[i][0] = zeitKonstantenStrecke[i];
			aST[i][1] = 1;
			temp = MathArrays.convolve(temp, aST[i]);					//(1+sT1)(1+sT2)(1+sT3).....(1+sTn) falten
			//System.out.println("aST["+i+"]: "+Arrays.toString(aST[i])+" tmp: "+Arrays.toString(tmp));
		}
		aS= temp;				//Nennerpolyonom Strecke
		bS[0] = streckenbeiwert;	//Z�hlerpolyonom Strecke	
		
		switch (type){	
		case Model.piRegler:
			bR = new double[2];			// Z�hlerpoly = s*kR*Tn+kR
			bR[1] = reglerParameter[0];	//kR
			bR[0] = reglerParameter[0]*reglerParameter[1]; //s*kR*Tn
			
			aR = new double[2];			// Nennerpoly = sTn + 0
			aR[1] = 0;					// 0
			aR[0] = reglerParameter[1];	//s*Tn	
			this.b = MathArrays.convolve(bS, bR);
			this.a = MathArrays.convolve(aS, aR);
			break;
			
		case Model.pidRegler:
			bR = new double[3];			// Z�hlerpoly =s^2*kR*(Tn*Tp+Tn*Tv) + s*kR*(Tn+Tp) + kR]
			bR[2] = reglerParameter[0];	//kR
			bR[1] = reglerParameter[0]*(reglerParameter[1]+reglerParameter[3]); //s*kR*(Tn+Tp)
			bR[0] = reglerParameter[0]*reglerParameter[1]*(reglerParameter[3]+reglerParameter[2]); //s^2*kR*Tn*(Tp+Tv)
			
			aR = new double[3];			// Nennerpoly = s^2*Tn*Tp + s*Tn + 0]
			aR[2] = 0;					// 0
			aR[1] = reglerParameter[1];	//s*Tn	
			aR[0] = reglerParameter[1]*reglerParameter[3]; //s^2*Tn*Tp
			this.b = MathArrays.convolve(bS, bR);// B(s)
			this.a = MathArrays.convolve(aS, aR);// A(s)
			//System.out.println("aR: "+Arrays.toString(aR)+" aS: "+Arrays.toString(aS));
			//System.out.println("bR: "+Arrays.toString(bR)+" bS: "+Arrays.toString(bS));
			break;
		}
		
		
		//A(s) = A(s) + B(s)
		for (int i = 0; i < a.length; i++) {
			if (i<a.length-b.length){
				a[i] = a[i];
			} else {
				a[i] = a[i]+b[i-(a.length-b.length)];
			}
		}
		//System.out.println("a: "+Arrays.toString(a)+" b: "+Arrays.toString(b));
		
		//Vorebereitung f�r FFT
		this.schrittIfft();
		
		
	}
	
	public void  schrittIfft(){
		double [] freqAchse = new double[n/2];
		double abtastRate = 1/ws;
		Complex []symVekt = new Complex[n];		//Symetrischen Vektor
		Complex []impulsAComp = new Complex[n];		//Impulsantwort
		double []impulsA= new double[n];		//Impulsantwort

		
		freqAchse = MathLibrary.linspace(0, ws*Math.PI, (n/2));
		//System.out.println("freqAchse: "+Arrays.toString(freqAchse));
		
		Complex[] freqG = MathLibrary.freqs(b, a, freqAchse);	//Frequenzgang
		//System.out.println("freqG: "+Arrays.toString(freqG));
		
		for (int i = 0; i < n/2; i++) {			//Erster Teil des sym. Vektors
			symVekt[i]= freqG[i];
		}
		symVekt[n/2] = Complex.ZERO; 			// Mitte sym. Vektor
		for (int i = n-1; i > n/2; i--) {	 	//Zweiter Teil des sym. Vektors
			symVekt[i]= freqG[n-i].conjugate();
		}
		//System.out.println("symVek: "+Arrays.toString(symVekt));
		// symVekt padden für FFT
		//symVekt = MathLibrary.prepareForFFT(symVekt);
		
		//System.out.println("SymVek prepared: "+Arrays.toString(symVekt));
		
		//Impulsantwort
		FastFourierTransformer mytransformer = new FastFourierTransformer(DftNormalization.STANDARD);
		impulsAComp = mytransformer.transform(symVekt, TransformType.INVERSE);	
		//System.out.println("impulsAComp length: "+impulsAComp.length+" impulsA length: "+impulsA.length);
		
		for (int i = 0; i < n; i++) { // Durchläuft bis n, da zuvor gepaddete Werte abgschnitten werden sollen
			impulsA[i]=impulsAComp[i].getReal();				
		}
		//Schrittantwort
		this.yAxis = new double [impulsA.length];
		for (int i = 1; i < impulsA.length; i++) {
			this.yAxis[i]= this.yAxis[i-1]+impulsA[i];
		}

		//Zeitachse
		this.tAxis = MathLibrary.linspace(0, (this.yAxis.length-1)*abtastRate, this.yAxis.length);
		//System.out.println("yAxis.length-1: "+(yAxis.length-1)+" abtastRate: "+abtastRate);
		//System.out.println("yAxis length: "+yAxis.length+" tAxis length: "+tAxis.length);
		//System.out.println("tAxis StepResponse: "+Arrays.toString(tAxis));
			
	}
	
	
	
	public double[]  getyAxis(){
		return this.yAxis;
	}
	
	public double[]  gettAxis(){
		return this.tAxis;
	}

}
