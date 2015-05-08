import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.MathArrays;


public class StepResponse {
	
	//a = Z�hlerpolynom/b=Reglerpolynom/S= Strecke/R=Regler/T=Terme
	private double [] a;
	private double [] b;
	private double [] yAxis;
	private double [] tAxis;
	private int n;
	private double fs;
	private double [] tmp;
	
	public StepResponse(){
	}
	
	
	public void calc(int type, double[] reglerParameter, double streckenbeiwert, double [] zeitKonstantenStrecke, double[] kreisFrequenzspektrum){
		double [][] aST;
		double  bS[] = new double[1];	
		double [] aS;	
		double [] aR;
		double [] bR;
		
		
		aST = new double[zeitKonstantenStrecke.length][2]; 			//Speicher die Terme (1+sT1)(1+sT2)(1+sT3).....(1+sTn)
		tmp = new double[1];
		tmp[0] = 1;
		System.out.println("tmp: "+Arrays.toString(tmp));
		for (int i = 0; i < zeitKonstantenStrecke.length; i++) {
			aST[i][0] = zeitKonstantenStrecke[i];
			aST[i][1] = 1;
			tmp = MathArrays.convolve(tmp, aST[i]);					//(1+sT1)(1+sT2)(1+sT3).....(1+sTn) falten
			System.out.println("aST["+i+"]: "+Arrays.toString(aST[i])+" tmp: "+Arrays.toString(tmp));
		}
		aS= tmp;				//Nennerpolyonom Strecke
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
			System.out.println("aR: "+Arrays.toString(aR)+" aS: "+Arrays.toString(aS));
			System.out.println("bR: "+Arrays.toString(bR)+" bS: "+Arrays.toString(bS));
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
		System.out.println("a: "+Arrays.toString(a)+" b: "+Arrays.toString(b));
		
		//Vorebereitung f�r FFT
		fs = kreisFrequenzspektrum[kreisFrequenzspektrum.length-1];	//fs als max. Kreisfrequenz des w-Arrays gew�hlt
		n = MathLibrary.makePowOf2(kreisFrequenzspektrum.length); 
		this.schrittIfft();
		
		
	}
	
	public void  schrittIfft(){
		double [] freqAchse = new double[n/2];
		double abtastRate = 1/fs;
		Complex []symVekt = new Complex[n];		//Symetrischen Vektor
		Complex []impulsAComp = new Complex[n];		//Impulsantwort
		double []impulsA= new double[n];		//Impulsantwort
		double [] yAxisTmp;

		
		freqAchse = MathLibrary.linspace(0, fs*Math.PI, (n/2));
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
		System.out.println("symVek: "+Arrays.toString(symVekt));
		// symVekt padden für FFT
		//symVekt = MathLibrary.prepareForFFT(symVekt);
		
		//System.out.println("SymVek prepared: "+Arrays.toString(symVekt));
		
		//Impulsantwort
		FastFourierTransformer mytransformer = new FastFourierTransformer(DftNormalization.STANDARD);
		impulsAComp = mytransformer.transform(symVekt, TransformType.INVERSE);	
		System.out.println("impulsAComp length: "+impulsAComp.length+" impulsA length: "+impulsA.length);
		
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
		System.out.println("yAxis length: "+yAxis.length+" tAxis length: "+tAxis.length);
			
	}
	
	
	
	public double[]  getyAxis(){
		return this.yAxis;
	}
	
	public double[]  gettAxis(){
		return this.tAxis;
	}

}
