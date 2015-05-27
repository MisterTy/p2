package Main;

import java.util.Scanner;

import javax.swing.JFrame;

import Aux.AutoTest;
import Controller.Controller;
import Model.Model;



/**
 * 
 */
public class MainApplication extends JFrame{
	
	final static boolean DEBUGMODE = false;

    /**
     * 
     */
    public MainApplication()  {
    }

    /**
     * @param args 
     * @return
     */
    public static void main(String[] args) {
    	// Options for calculaitons
        int anzahlPunkte = 4000;
        
        Model smartLoopModel = new Model();
        
        if (DEBUGMODE){
        	final Scanner scanner = new Scanner( System.in );
        	
        	System.out.print("Automatic Test Nr. (0 for manual entry): ");
        	int testNr = Integer.parseInt(scanner.nextLine());
        	if (testNr == 0){
        		AutoTest.manualTest(smartLoopModel, scanner, anzahlPunkte);
        	} else {
        		AutoTest.executeTest(smartLoopModel, testNr, anzahlPunkte);
        	}
        	System.out.println("-----Done-----");
        	scanner.close();
        } else {
        	Controller smartLoopController = new Controller(smartLoopModel);
        	smartLoopModel.addObserver(smartLoopController);
            smartLoopController.startGui();
        }
        
        
        
        
        
        // Sequence for testing
    	
    }

}