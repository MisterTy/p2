
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
    
    
    public static double[] linspace(int start,int end,int n){
    	double space[] =new double[n];    	
    	for(double i = 0;i<n;i++) space[(int)i]=start+i*(end-start)/(n-1);
   		return space;   	
    }
    
    public static double[] linspace(int start,int end){
    	double space[] =new double[100];    	
    	for(double i = 0;i<100;i++) space[(int)i]=start+i*(end-start)/(99);
		return space;
    	
    }
    public static double[] logspace(int start,int end,int n){
    	double[] space=linspace(start,end,n);
    	for (int i = 0; i < n; i++) space[i]=Math.pow(10, space[i]);
		return space;    	
    }
    
    public static double[] logspace(int start,int end){
    	double[] space=linspace(start,end,100);
    	for (int i = 0; i < 100; i++) space[i]=Math.pow(10, space[i]);
		return space;
    	
    }

}