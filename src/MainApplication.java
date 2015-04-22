
import java.awt.Dimension;

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
        
        SwingUtilities.invokeLater(new Runnable() {
     			public void run() {
     				 try {
     				 UIManager
     				 .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
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
        
        
        saniPOC saniResult = new saniPOC();
        saniResult.saniCalculation();
    }

}