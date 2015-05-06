import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.MathArrays;


public class ReglerTest extends JFrame{
	
	static int anzahlPunkte=100;
	static int n=5;
	static double ks=1;
	static double[] T ={5.7821,6.7547,7.8909,9.2183,10.7689};
	static double Tmax=findMax(T);
	static double Tmin=findMin(T);

	public static void main(String[] args) {
		
		double[] a={9,5,6};
		double[] b={1,0,1};
		double[] w={5,6,7,8,9};
		System.out.println(Arrays.toString(MathLibrary.freqs(a, b, w)));
		
		double[] antwort=null;

		FaustformelRegler ffr=new FaustformelRegler();
		
		
    	System.out.println(Arrays.toString(ffr.Oppelt(30, 20, 0.5,Model.pidRegler)));
    	
    	
       	antwort=Phasengangmethode(MathLibrary.logspace(Math.log10(1/(Tmax*10)),Math.log10(1/(Tmin/10)),anzahlPunkte),ks,Math.PI/4);
    	
    	
    	
	}
	
	private static double[] Phasengangmethode(double[] w,double ks,double phir){
		double[] ans = null;
		Complex[] Gs=new Complex[anzahlPunkte];
		
		for (int i = 0; i < Gs.length; i++) Gs[i]=new Complex(1, 0);
		for (int i = 0; i < n; i++) {
    		for (int j = 0; j < Gs.length; j++) {
    			Gs[j]=Gs[j].multiply(((Complex.I.multiply(w[j]).multiply(T[i]).add(1))).reciprocal());
				
			}
			
		}
		
		for (int i = 0; i < Gs.length; i++) Gs[i]=Gs[i].multiply(ks);//Gs Liste komp.

		
		PIRegler piRegler=new PIRegler();
		piRegler.setValues(Gs, w, phir, ks, T);
		piRegler.compute();
		System.out.println(Arrays.toString(piRegler.getResult()));
		return ans;
		
	}
	
	private static double findMax(double[] werte){
		double maximalwert=werte[0];
		for (int i = 0; i < werte.length; i++) if(werte[i]>maximalwert)maximalwert=werte[i];
		return maximalwert;
	}
	private static double findMin(double[] werte){
		double maximalwert=werte[0];
		for (int i = 0; i < werte.length; i++) if(werte[i]<maximalwert)maximalwert=werte[i];
		return maximalwert;
	}
}

    