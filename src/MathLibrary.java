
import java.util.*;

import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class MathLibrary {

    /**
     * 
     */
    public MathLibrary() {
    	
    	
    }
    
    /**
     * Methode sucht index eines Punktes (Phi) aus einem Double Array (phase).
     * Gibt ein Integer Array zurück mit 2 Indexwerten, 
     * index unterhalb (0) und oberhalb (1) des gesuchten Punktes.
     * @param phase
     * @param phi
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
     * Gibt eine linear unterteilte Werteliste als Double Array zurück mit n Punkten
     * @param start
     * @param end
     * @param n
     * @return lineare Werteliste
     */
    public static double[] linspace(double start,double end,int n){
    	double space[] =new double[n];    	
    	for(int i = 0;i<n;i++) space[i]=start+i*(end-start)/(n-1);
   		return space;   	
    }
    
	/**
	 * Gibt eine linear unterteilte Werteliste als Double Array zurück mit 100 Punkten
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
     * Gibt eine Logarithmisch unterteile Werteliste von 10^start bis und mit 10^ende mit n Anzahl Punkten zurück.
     * @param start
     * @param end
     * @param n
     * @return Werteliste
     */
    public static double[] logspace(double start,double end,int n){
    	double[] space=linspace(start,end,n);
    	for (int i = 0; i < n; i++) space[i]=Math.pow(10, space[i]);
		return space;    	
    }
    
    /**
     * Gibt eine Logarithmisch unterteile Werteliste von 10^start bis und mit10^ende mit 100 Punkten zurück
     * @param start
     * @param end
     * @return Werteliste
     */
    public static double[] logspace(double start,double end){
    	double[] space=linspace(start,end,100);
    	for (int i = 0; i < 100; i++) space[i]=Math.pow(10, space[i]);
		return space;
    	
    }

}