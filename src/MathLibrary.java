
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
    
    
    public static int[] linspace(int start,int end,int n){
		return null;
    	
    }
    public static int[] linspace(int start,int end){
    	int n=100;
		return null;
    	
    }
    public static int[] logspace(int start,int end,int n){
		return null;
    	
    }
    public static int[] logspace(int start,int end){
    	int n=100;
		return null;
    	
    }

}