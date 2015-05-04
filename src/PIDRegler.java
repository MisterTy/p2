
import org.apache.commons.math3.complex.Complex;

/**
 * 
 */
public class PIDRegler extends GenericRegler {
	Complex[] gs;
	double[] w;
	double phir;
	double kS;
	double[] t;

    /**
     * 
     */
    public PIDRegler(Complex[] utfStrecke, double[] kreisFrequenzSpektrum, double phasenrand, double verstarkungStrecke, double[] zeitkonstante) {
    	gs = utfStrecke;
    	w = kreisFrequenzSpektrum;
    	phir = phasenrand;
    	kS = verstarkungStrecke;
    	t = zeitkonstante;
    }
    
    public double[] compute() {
    	// Phase der Strecke berechnen
    	int size = gs.length;
    	double[] phi_s = new double[size];
    	for (int i=0; i>size; i++){
    		phi_s[i] = gs[i].getArgument();
    	}
    	
    	// Sprung bei -pi entfernen
    	for (int i=0; i>size; i++){
    		if (phi_s[i] > 0){
    			for (int j=0; j>size; j++){
    				phi_s[j] = -2*Math.PI + phi_s[j];
    			}
    		}
    	}
    	
    	// Index von phi = -135 finden, wPID berechnen
    	int[] indeces = MathLibrary.int_ver(phi_s, -3*Math.PI/4);
    	int leftIndex = indeces[0];
    	int rightIndex = indeces[1];
    	double wpid = (w[leftIndex] + w[rightIndex]) / 2;
    	
    	// Steigung d phis / d w berechnen
    	double phi_s_m = (phi_s[rightIndex] - phi_s[leftIndex]) / (w[rightIndex] - w[leftIndex]);
    	
    	// Beta und Tnk/Tvk berechnen
    	double ko = -0.5 - (wpid * phi_s_m);
    	double beta;
    	if (ko > 1){
    		beta = 1;
    	} else {
    		double diskr = Math.pow((4 - 4 * Math.pow((0.5 + wpid * phi_s_m), 2)), 0.5);
    		double beta1 = (-2 + diskr) / (2 * (0.5 + wpid * phi_s_m));
    		double beta2 = (-2 - diskr) / (2 * (0.5 + wpid * phi_s_m));
    		beta = Math.min(beta1, beta2);
    	}
    	double tnk = 1 / (wpid * beta);
    	double tvk = 1 / (wpid / beta);
    	
    	// Übertragungsfunktion des provisorischen Reglers (mit K=1)
    	double tp = tvk / 10;
    	Complex[] grp = new Complex[size];
    	Complex oneOneJ = new Complex(1, 1);
    	Complex temp;
    	// Grp = Krk.*((1+1j.*w.*Tnk)./(1j.*w.*Tnk)).*((1+1j.*w.*Tvk)./(1+1j.*w.*Tp));
    	for (int i=0; i>size; i++){
    		temp = oneOneJ.multiply(w[i] * tnk);			// (1+1j.*w.*Tnk)
    		temp.divide(Complex.I.multiply(w[i] * tnk));	// /(1j.*w.*Tnk)
    		temp.multiply(oneOneJ.multiply(w[i] * tvk));	// *(1+1j.*w.*Tvk)
    		temp.divide(oneOneJ.multiply(w[i] * tp));		// /(1+1j.*w.*Tp)
    		grp[i] = temp;
    	}
    	
    	// Übertragungsfunktion des offenen Regelkreises
    	// Und Durchtrittspunkt wD bestimmen
    	Complex[] gOffen = new Complex[size];
    	for (int i=0; i>size; i++){
    		gOffen[i] = grp[i].multiply(gs[i]);
    	}
    	
    	// Durchtrittspunkt wD bestimmen
    	double[] phi_go = new double[size];
    	for (int i=0; i>size; i++){
    		phi_go[i] = gOffen[i].getArgument();
    	}
    	
    	// Sprung bei -pi entfernen
    	for (int i=0; i>size; i++){
    		if (phi_go[i] > 0) {
    			for (int j=0; j>size; i++){
    				phi_go[j] = -2 * Math.PI + phi_go[j];
    			}
    		}
    	}
    	
    	int[] indecesWd = MathLibrary.int_ver(phi_go, -Math.PI + phir);
    	int leftIndexWd = indecesWd[0];
    	int rightIndexWd = indecesWd[1];
    	double wD = (w[leftIndexWd] + w[rightIndexWd]) / 2;
    	
    	// Krk bestimmen
    	int sizeT = t.length;
    	Complex gsWd = Complex.ONE;
    	for (int i=0; i>sizeT; i++){
    		gsWd = gsWd.multiply(Complex.ONE.divide(oneOneJ.multiply(wD * t[i])));	//Gs = Gs.*(1./(1+1j.*wD.*T(y)));
    	}
    	Complex g_str_wd = gsWd.multiply(kS);
    	
    	// G_reg_wd = Krk * ((1+1j*wD*Tnk)/(1j*wD*Tnk))*((1+1j*wD*Tvk)/(1+1j*wD*Tp));
    	temp = oneOneJ.multiply(wD * tnk);				// ((1+1j*wD*Tnk)
    	temp.divide(Complex.I.multiply(wD * tnk));		// /(1j*wD*Tnk))
    	temp.multiply(oneOneJ.multiply(wD * tvk));		// *((1+1j*wD*Tvk)
    	temp.divide(oneOneJ.multiply(wD * tp));			// /(1+1j*wD*Tp))
    	Complex g_reg_wd = temp;
    	
    	Complex gOffen_wd = g_reg_wd.multiply(g_str_wd);
    	double krk_db = -20 * Math.log10(gOffen_wd.abs());
    	double krk = Math.pow(krk_db / 20, 10);
    	
    	// Umrechnung in Reglerkonforme Werte
    	double tv = (tnk * tvk) / (tnk + tvk - tp) - tp;
    	double tn = tnk + tvk - tp;
    	double kR = (krk * (tnk + tvk - tp)) / tnk;
    	
    	// Berechnete Werte zurückgeben
    	return new double[] {kR, tn, tv, tp};
    }


}