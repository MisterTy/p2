package Model;

import java.util.*;


//TODO: Klasse entfernen
/**
 * 
 */
public class FaustformelRegler {
	
	double kR=0;
	double Tn=0;
	double Tv=0;
	double Tp=0;

    /**
     * 
     */
    public FaustformelRegler() {
    }
    
    public double[] Oppelt(double Tu, double Tg, double Ks, int type){
    	if(type == Model.piRegler){
    		kR=0.8/Ks*Tg/Tu;
    		Tn=3*Tu;   		
    	}
       	else if(type==Model.pidRegler){
       		kR=1.2/Ks*Tg/Tu;
       		Tn=2*Tu;
       		Tv=0.42*Tu;
       	}
    	else System.out.println("Ungültiger Reglertyp");
    		
		return new double[] {kR,Tn,Tv,Tp};
    	
    }
    
    public double[] Rosenberg(double Tu, double Tg, double Ks, int type){
    	if(type == Model.piRegler){
    		kR=0.91/Ks*Tg/Tu;
    		Tn=3.3*Tu;   		
    	}
       	else if(type==Model.pidRegler){
       		kR=1.2/Ks*Tg/Tu;
       		Tn=2*Tu;
       		Tv=0.44*Tu;
       	}
    	else System.out.println("Ungültiger Reglertyp");
    		
		return new double[] {kR,Tn,Tv,Tp};
    	
    }

}