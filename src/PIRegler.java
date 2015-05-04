
import java.util.*;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class PIRegler extends GenericRegler {
	Complex[] Gs;
	double[] phi_s; 
	double[] w;
	double Tn;
	Complex[] Grp;

   /**
    * 
    * @param Gs
    * @param w
    * @param phir
    * @param kS
    * @param T
    * @return[kR,Tn,Tv,Tp]
    */
    public PIRegler(Complex[] Gs, double w[],double phir,double kS, double[] T) {

    	this.Gs=Gs;
    	this.w=w;
    	phi_s= new double[Gs.length];
    	Grp=new Complex[Gs.length];
    	
    	
    	
    }

    public void berechnungTn(){
    	
    	
    	for (int i = 0; i < Gs.length; i++)  phi_s[i] = Gs[i].getArgument();
    	for (int y = 0; y < Gs.length; y++) {
    		if(phi_s[y]>0){
    			for(int z=y;y < Gs.length; z++)	phi_s[z]=-2*Math.PI+phi_s[z];
    		}			
		}
    	
    	int ind_left=MathLibrary.int_ver(phi_s, -(Math.PI/2))[0];
    	int ind_right=MathLibrary.int_ver(phi_s, -(Math.PI/2))[1];
    	
    	double wpi=(w[ind_left]+w[ind_right])/2;
    	Tn=1/wpi;
   	
       	for (int i = 0; i < w.length; i++) {
			Grp[i]=(Complex.I.multiply(w[i]).multiply(Tn)).reciprocal().add(1); //Übertragungsfkt PI Regeler mit kR=1
		}
       	System.out.println("Tn: "+Tn);
       	System.out.println("Grp "+Arrays.toString(Grp));
 	}
    
    


}