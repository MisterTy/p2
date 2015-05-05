
import java.util.*;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

/**
 * Diese Klasse berechnet die Parameter eines PI-Reglers nach dem Zellwegerverfahren.
 * @author Dominik
 *
 */
public class PIRegler extends GenericRegler {
	
	
	private double[] phi_s; 
	private double[] w;
	private double[] phi_rprov;
	private double[] phi_0;
	private double[] t;
	
	private double tn;
	private double tv;
	private double tp;
	private double kS;
	private double kR;
	private double phir;
	private double wD;
	
	private Complex[] grp;
	private Complex[] gs;
	
	private Complex grp_wd;
	private Complex g_str_wd;
	private Complex gOffwd;

  /**
   * Konstruktor des PI Reglers
   * 
   * @param gs
   * @param w
   * @param phir
   * @param kS
   * @param T
   */
    public PIRegler(Complex[] gs, double w[],double phir,double kS, double[] T) {

    	int lGs=gs.length;
    	this.gs=gs;
    	this.w=w;
    	this.phir=phir;
    	this.t=T;
    	this.kS=kS;
    	
    	tv=-1;
    	tp=-1;
    	
    	phi_s= new double[lGs];
    	phi_rprov= new double[lGs];
    	phi_0= new double[lGs];
    	grp= new Complex[lGs];
    	
  }

    
    private void berechnungTn(){
    	
    	
    	for (int i = 0; i < gs.length; i++)  phi_s[i] = gs[i].getArgument();
    	
       	phi_s=sprungEntfernen(phi_s);
    	    	
    	int ind_left=MathLibrary.int_ver(phi_s, -(1.57079))[0];
    	int ind_right=MathLibrary.int_ver(phi_s, -(1.57079))[1];
    	
    	double wpi=(w[ind_left]+w[ind_right])/2;
    	tn=1/wpi;
   	
       	for (int i = 0; i < w.length; i++) {
			grp[i]=(Complex.I.multiply(w[i]).multiply(tn)).reciprocal().add(1); //Übertragungsfkt PI Regeler mit kR=1
		}
 	}
    
    private void berechnungWD(){
    	for (int i = 0; i < grp.length; i++) phi_rprov[i]=grp[i].getArgument();
    	for (int i = 0; i < grp.length; i++) phi_0[i]=phi_s[i]+phi_rprov[i];
    	
    	phi_0=sprungEntfernen(phi_0);
    	
    	int ind_left=MathLibrary.int_ver(phi_0, -Math.PI+phir)[0];
    	int ind_right=MathLibrary.int_ver(phi_0, -Math.PI+phir)[1];
    	wD=((w[ind_left]+w[ind_right])/2);
    	grp_wd=(Complex.I.multiply(wD).multiply(tn).reciprocal()).add(1);
    }
    
    private void berechnungStrecke(){
    	g_str_wd=Complex.ONE;
    	for (int i = 0; i < t.length; i++) g_str_wd=((Complex.I.multiply(wD).multiply(t[i]).add(1)).reciprocal()).multiply(g_str_wd);			    	
    	g_str_wd=g_str_wd.multiply(kS);
   }
    
    private void berechnungOffenerRegelkreis(){
    	gOffwd=grp_wd.multiply(g_str_wd);
    	double amplOffwd=20*FastMath.log10(gOffwd.abs());
    	double kRdb=-amplOffwd;
    	kR=FastMath.pow(10, (kRdb/20));
    	gOffwd=grp_wd.multiply(g_str_wd);
    }
    /**
     * Gibt Werte des PI Reglers in der Konsole aus.
     */
    public void konsoleOut(){
    	System.out.println("kR: "+kR);
    	System.out.println("Tn: "+tn);
    	System.out.println("Tv:" +tv);
    	System.out.println("Tp:" +tp);
    }
    /**
     * Führt die Berechnung des PI Reglers durch
     */
    public void compute(){
    	berechnungTn();
    	berechnungWD();
    	berechnungStrecke();
    	berechnungOffenerRegelkreis();
    	
    }
    
    /**
     * Neue Gs Liste übergeben
     * @param gs
     */
    public void setGs(Complex[] gs){
    	this.gs=gs;
    }
    
    /**
     * Neue Omega Liste übergeben
     * @param w
     */
    public void setW(double[] w){
    	this.w=w;
    }
    /**
     * Neuer Phasenrand übergeben
     * @param phir
     */
    public void setPhir(double phir){
    	this.phir=phir;
    }
    
    /**
     * Neue Verstärkung übergeben
     * @param kS
     */
    public void setkS(double kS){
    	this.kS=kS;
    }
    /**
     * Neue Zeitkonstanten übergeben
     * @param t
     */
    public void setT(double[] t){
    	this.t=t;
    }
    
    
    
    


}