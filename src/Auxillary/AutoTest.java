package Auxillary;
import java.util.Arrays;
import java.util.Scanner;

import Model.Model;


public class AutoTest {
	public static void executeTest(Model model, int testNr, int anzahlPunkte){
		boolean valid = false;
		double tu=-1; double tg=-1; double phir=-1; double ks=-1; int typ=-1;
		switch (testNr){
			case 1:		// Strecke 1 PID
				valid = true;
				tu = 3.08; tg = 30.8; phir = 23.3; ks = 0.5; typ = 2;
	            break;
			case 2:		// Strecke 1 PI
				valid = true;
				tu = 3.08; tg = 30.8; phir = 23.3; ks = 0.5; typ = 1;
	            break;
			case 3:		// Strecke 7 PID
				valid = true;
				tu = 16.6; tg = 41.7; phir = 23.3; ks = 1; typ = 2;
				break;
			case 4:		// Strecke 7 PI
				valid = true;
				tu = 16.6; tg = 41.7; phir = 23.3; ks = 1; typ = 1;
				break;
	        default:
	        	valid = false;
	        	System.out.println("Invalid TestNr");
	        	break;
		}
		if (valid){
			System.out.println("Tu: "+tu+" Tg: "+tg+" phir: "+phir+" ks: "+ks+" typ: "+typ);
			model.setAnzahlPunkte(anzahlPunkte);
            model.setStrecke(tu, tg);
            model.setUberschwingen(phir);
            model.setVerstarkung(ks);
            model.addRegelkreis(typ);
            model.output();
            //System.out.println("X-Values: "+Arrays.toString(model.regelKreisListe.get(0).getXValues()));
            //System.out.println("Y-Values: "+Arrays.toString(model.regelKreisListe.get(0).getYValues()));
		}
	}
	
	public static void manualTest(Model model, Scanner scanner, int anzahlPunkte){
		System.out.print("Enter Value for Tu: ");
        double tu = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Tg: ");
        double tg = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Overschoot: ");
        double phir = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Verstarkung: ");
        double verstarkung = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Type (1 -> PIRegler, 2 -> PIDRegler): ");
        int reglerTyp = Integer.parseInt(scanner.nextLine());
        
        model.setAnzahlPunkte(anzahlPunkte);
        model.setStrecke(tu, tg);
        model.setUberschwingen(phir);
        model.setVerstarkung(verstarkung);
        model.addRegelkreis(reglerTyp);
        model.output();
		
	}
}
