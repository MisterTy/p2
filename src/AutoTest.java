import java.util.Scanner;


public class AutoTest {
	public static void executeTest(Model model, int testNr, int anzahlPunkte){
		boolean valid = false;
		double tu=-1; double tg=-1; double phir=-1; double ks=-1; int typ=-1;
		switch (testNr){
			case 1:
				valid = true;
				tu = 3.08; tg = 30.8; phir = 0.785; ks = 0.5; typ = 2;
	            break;
	        default:
	        	valid = false;
	        	System.out.println("Invalid TestNr");
	        	break;
		}
		if (valid){
			System.out.println("Tu: "+tu+" Tg: "+tg+" phir: "+phir+" ks: "+ks+" typ: "+typ);
			model.setAnzahlPunkte(anzahlPunkte);
			System.out.println("after setPunkte");
            model.setStrecke(tu, tg);
            System.out.println("after setStrecke");
            model.setPhasenrand(phir);
            System.out.println("after setPhasenrand");
            model.setVerstarkung(ks);
            System.out.println("after setVerstarkung");
            model.addRegelkreis(typ);
            System.out.println("after addRegelkreis");
            model.output();
		}
	}
	
	public static void manualTest(Model model, Scanner scanner, int anzahlPunkte){
		System.out.print("Enter Value for Tu: ");
        double tu = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Tg: ");
        double tg = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Phasenrand: ");
        double phir = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Verstarkung: ");
        double verstarkung = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Value for Type (1 -> PIRegler, 2 -> PIDRegler): ");
        int reglerTyp = Integer.parseInt(scanner.nextLine());
        
        model.setAnzahlPunkte(anzahlPunkte);
        model.setStrecke(tu, tg);
        model.setPhasenrand(phir);
        model.setVerstarkung(verstarkung);
        model.addRegelkreis(reglerTyp);
        model.output();
		
	}
}
