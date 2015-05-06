import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.MathArrays;


public class StepResponse {
	
	//a = Zählerpolynom/b=Reglerpolynom/S= Strecke/R=Regler/T=Terme
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
		tmp  = aST[0];												//erster Term (1+sT1)
		for (int i = 1; i < zeitKonstantenStrecke.length; i++) {
			tmp = MathArrays.convolve(tmp, aST[i]);					//(1+sT1)(1+sT2)(1+sT3).....(1+sTn) falten
		}
		aS= tmp;				//Nennerpolyonom Strecke
		bS[0] = streckenbeiwert;	//Zählerpolyonom Strecke	
		
		switch (type){	
		case Model.piRegler:
			bR = new double[2];			// Zählerpoly = s*kR*Tn+kR
			bR[1] = reglerParameter[0];	//kR
			bR[0] = reglerParameter[0]*reglerParameter[1]; //s*kR*Tn
			
			aR = new double[2];			// Nennerpoly = sTn + 0
			aR[1] = 0;					// 0
			aR[0] = reglerParameter[1];	//s*Tn	
			this.b = MathArrays.convolve(bS, bR);
			this.a = MathArrays.convolve(aS, aR);
			break;
			
		case Model.pidRegler:
			bR = new double[3];			// Zählerpoly =s^2*kR*(Tn*Tp+Tn*Tv) + s*kR*(Tn+Tp) + kR]
			bR[2] = reglerParameter[0];	//kR
			bR[1] = reglerParameter[0]*(reglerParameter[1]+reglerParameter[3]); //s*kR*(Tn+Tp)
			bR[0] = reglerParameter[0]*reglerParameter[1]*(reglerParameter[3]+reglerParameter[2]); //s^2*kR*Tn*(Tp+Tv)
			
			aR = new double[3];			// Nennerpoly = s^2*Tn*Tp + s*Tn + 0]
			aR[2] = 0;					// 0
			aR[1] = reglerParameter[1];	//s*Tn	
			aR[0] = reglerParameter[1]*reglerParameter[3]; //s^2*Tn*Tp
			this.b = MathArrays.convolve(bS, bR);// B(s)
			this.a = MathArrays.convolve(aS, aR);// A(s)
			break;
		}
		
		//A(s) = A(s) + B(s)
		for (int i = 0; i < a.length; i++) {
			a[i]= a[i];
			if (i>b.length){
				a[i] = a[i]+b[i-b.length];
			}
		}
		
		//Vorebereitung für FFT
		fs = kreisFrequenzspektrum[kreisFrequenzspektrum.length];	//fs als max. Kreisfrequenz des w-Arrays gewählt
		n = kreisFrequenzspektrum.length; 
		this.schrittIfft();
		
		
	}
	
	public void  schrittIfft(){
		double [] freqAchse = new double[n/2];
		double abtastRate = 1/fs;
		Complex []tempH = new Complex[n];		//Temporärer ordner für symetrischen Vektor für iff
		Complex []impulsA = new Complex[n];		//Impulsantwort
		
		freqAchse = MathLibrary.linspace(0, fs*Math.PI, (n/2));
		
		Complex[] H = MathLibrary.freqs(b, a, freqAchse);
		
		for (int i = 0; i < n/2; i++) {			//Erster Teil des sym. Vektors
			tempH[i]= H[i];
		}
		tempH[n/2] = Complex.ZERO; 				// Mitte sym. Vektor
		for (int i = n-1; i > n/2; i--) {	 	//Zweiter Teil des sym. Vektors
			tempH[i]= H[i];
		}
		FastFourierTransformer mytransformer = new FastFourierTransformer(DftNormalization.STANDARD);
		impulsA = mytransformer.transform(H, TransformType.INVERSE);

		yAxis = MathArrays.convolve(arg0, arg1)
		
		
		
		
		
		
		
	}
	
	
	
	public double[]  getyAxis(){
		return this.yAxis;
	}
	
	public double[]  gettAxis(){
		return this.tAxis;
	}

}
