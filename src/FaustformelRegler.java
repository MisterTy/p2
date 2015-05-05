
import java.util.*;

/**
 * 
 */
public class FaustformelRegler extends GenericRegler {

    /**
     * 
     */
    public FaustformelRegler() {
    }
    
    public double[] Oppelt(double Tu, double Tg, double Ks, String type){
    	if(type.equals("PI")) System.out.println("PI");
    	
    	else if(type.equals("PID")) System.out.println("PID");
    	else System.out.println("Ungültiger Reglertyp");
    		
		return null;
    	
    }

}