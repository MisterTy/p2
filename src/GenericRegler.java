
import java.util.*;

/**
 * 
 */
public class GenericRegler {
	public int reglerTyp = 0;
	double[] result = new double[4];

    /**
     * 
     */
    public GenericRegler() {
    }
    
    public double[] getResult(){
    	return result;
    }
    
    public double[] sprungEntfernen(double[] liste){
       	for (int y = 0; y < liste.length; y++) {
    		if(liste[y]>0){
    			for(int z=y;z < liste.length; z++)	liste[z]=-2*Math.PI+liste[z];
    			break;
    		}   		
		}
		return liste;
    	
    }

}