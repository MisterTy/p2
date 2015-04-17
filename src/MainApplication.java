
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.math3.*; //TODO Reduce Import to include only the needed packages (streamlining)

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
        // TODO implement here
        System.out.println("Hallo Welt");
        
        saniPOC saniResult = new saniPOC();
        saniResult.saniCalculation();
    }

}