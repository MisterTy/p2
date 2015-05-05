
import java.awt.Dimension;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.math3.*; //TODO Reduce Import to include only the needed packages (streamlining)
import org.apache.commons.math3.complex.Complex;


/**
 * 
 */
public class MainApplication extends JFrame {

    /**
     * 
     */
    public MainApplication() {
    }

    /**
     * 
     */
    public void MainApplication() {
        // TODO implement here
    }

    /**
     * @param args 
     * @return
     */
    public static void main(String[] args) {
       System.out.println("hallo welt");
        SwingUtilities.invokeLater(new Runnable() {
     			public void run() {
     				 try {
     				 UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
     				 } catch (Exception exception) {
     					 exception.printStackTrace();
     				 }
     				 JFrame frame = new JFrame();

     				frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
     				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     				frame.setTitle("Smart Loop");
     				frame.getContentPane().add(new View());
     				frame.setSize(700,400);
     				frame.setMinimumSize(new Dimension(700, 500));
     				frame.setVisible(true);
     				
     			}
     		});

        //test logspace, int_ver
/*        double a[]=new double[100];
        a=MathLibrary.logspace(0,4,10);
        for (int i = 0; i < a.length; i++) {
			System.out.println(""+(i+1)+": "+a[i]);
		}       
        double hallo[]={10,9,8,7,6,5,4,3,2,1,0};
        double wert =7.7;

        System.out.println("Test int_ver: "+hallo[MathLibrary.int_ver(hallo,wert)[0]]+", "+hallo[MathLibrary.int_ver(hallo,wert)[1]]);
        */
        
        
        // Options for calculaitons
        int anzahlPunkte = 100;
        
        // Sequence for testing
        Model model = new Model();
        
    	final Scanner scanner = new Scanner( System.in );
    	
    	System.out.print("Automatic Test Nr. (0 for manual entry): ");
    	int testNr = Integer.parseInt(scanner.nextLine());
    	if (testNr == 0){
    		AutoTest.manualTest(model, scanner, anzahlPunkte);
    	} else {
    		AutoTest.executeTest(model, testNr, anzahlPunkte);
    	}
    	System.out.println("-----Done-----");
    	scanner.close();
    }

}