
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/**
 * Verwaltet eingaben, die druch das View gemacht werden.
 */
public class Controller implements Runnable {
	Model smartLoopModel;
	View smartLoopView;

    /**
     * @param model
     */
    public Controller(Model model) {
    	smartLoopModel = model;
    }
    
    public void run(){
    	try {
			 UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		 } catch (Exception exception) {
			 exception.printStackTrace();
		 }
		 JFrame frame = new JFrame();

		 frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setTitle("Smart Loop");
		 View view = new View();
		 view.setActionHandler(this);
		 smartLoopView = view;
		 frame.getContentPane().add(view);
		 frame.setSize(700,400);
		 frame.setMinimumSize(new Dimension(800, 550));
		 frame.setVisible(true);
    }
    
    public void startGui(){
    	SwingUtilities.invokeLater(this);
    }
    
    
    public void berechnenPressed(double tuValue, double tgValue, double kValue, boolean pidState, boolean piState){
    	if (validateValues(tuValue, tgValue, kValue, pidState, piState)){
    		smartLoopView.setState(View.calculatingState);
    		
    		int type = Model.pidRegler;
    		if (piState){
    			type = Model.piRegler;
    		}
    		
    		double phir = Math.PI/4;
    		// TODO phir durch sliderwert festlegen
    		
    		// Start Calculations
    		smartLoopModel.setAnzahlPunkte(1000);
    		smartLoopModel.setStrecke(tuValue, tgValue);
    		smartLoopModel.setPhasenrand(phir);
    		smartLoopModel.setVerstarkung(kValue);
    		smartLoopModel.addRegelkreis(type);
    		
    		smartLoopModel.output();
    		
    		// Update Modificaiton Fields
    		double[] result = smartLoopModel.getResult();
    		if (type == Model.piRegler){
    			smartLoopView.setState(View.modifyPIState);
    			smartLoopView.updateKr(result[0]);
    			smartLoopView.updateTn(result[1]);
    			smartLoopView.updateTv(0.0);
    			smartLoopView.updateTp(0.0);
    			
    		} else {
    			smartLoopView.setState(View.modifyPIDState);
    			smartLoopView.updateKr(result[0]);
    			smartLoopView.updateTn(result[1]);
    			smartLoopView.updateTv(result[2]);
    			smartLoopView.updateTp(result[3]);
    		}
    		
    		
    	}
    	else{
    		System.out.println("Somthing is wrong with entered values...");
    	}
    }
    
    public void clearPressed(){
    	smartLoopModel.removeRegelkreis();
    	smartLoopView.setState(View.initState);
    }
    
    /**
     * Überprüft ob die Werte für Tu, Tg und k plausibel sind.
     * Getestet wird:
     *  - Verhältnis tu/tg nicht zu gross und nicht zu klein
     *  - k nicht negativ
     *  - entweder PI oder PID selektiert
     *  
     * @param tuValue
     * @param tgValue
     * @param kValue
     * @return
     */
    private boolean validateValues(double tuValue, double tgValue, double kValue, boolean pidState, boolean piState){
    	double v = tuValue / tgValue;
		if ( v > 0.64173) {
			System.out.println("Tu/Tg too great --> N would be greater than 8");
			return false;
		}
		else if (v < 0.001) {
			System.out.println("Tu/Tg too small --> N would be less than 1");
			return false;
		}
		if (kValue < 0){
			System.out.println("k must be positive");
			return false;
		}
		if (! pidState ^ piState){
			return false;
		}
		return true;
    }
}