
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.data.xy.XYSeries;
/**
 * Verwaltet eingaben, die druch das View gemacht werden.
 */
public class Controller implements Runnable {
	Model smartLoopModel;
	View smartLoopView;
	
	int plotNummerierung=0;

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
		 frame.setSize(800,560);
		 frame.setMinimumSize(new Dimension(800, 560));
		 frame.setVisible(true);
    }
    
    public void startGui(){
    	SwingUtilities.invokeLater(this);
    }
    
    
    public void berechnenPressed(double tuValue, double tgValue, double kValue, boolean pidState, boolean piState, int sliderPhirValue){
    	if (validateValues(tuValue, tgValue, kValue, pidState, piState)){
    		smartLoopView.setState(View.calculatingState);
    		smartLoopView.updateConsole("Berechnete Werte werden ausgeben");
    		
    		int type = Model.pidRegler;
    		if (piState){
    			type = Model.piRegler;
    		}
    		double phir = Math.PI/4;
    		System.out.println("Slider Value: "+sliderPhirValue);
    		switch(sliderPhirValue){
    		case 0:
    				phir=Math.PI*76.3/180;
    			break;
    		case 1:
    				phir=Math.PI*65.5/180;
    			break;
    		case 2:
    			   phir=Math.PI*51.5/180;			
    			break;
    		case 3:
    			phir=Math.PI/4;
    			break;
    		}
    		
    		// Start Calculations
    		smartLoopModel.setAnzahlPunkte(1000);
    		smartLoopModel.setStrecke(tuValue, tgValue);
    		smartLoopModel.setPhasenrand(phir);
    		smartLoopModel.setVerstarkung(kValue);
    		smartLoopModel.addRegelkreis(type);
    		
    		smartLoopModel.output();
    		
			//TODO Schrittantwort aus Werten berechnen
			//TODO plot() mit daten aus Schrittantwort
    		
    		addplot(smartLoopModel.getXValues(), smartLoopModel.getYValues());


    		
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
    	smartLoopView.updateConsole("Neue Werte können eingegeben werden...");
    }
    
    public void krUpdated(int newValue){
    	smartLoopView.updateParam(newValue / 1000.0, "kr");
    }
    public void tnUpdated(int newValue){
    	smartLoopView.updateParam(newValue / 1000.0, "tn");
    }
    public void tvUpdated(int newValue){
    	smartLoopView.updateParam(newValue / 1000.0, "tv");
    }
    public void tpUpdated(int newValue){
    	smartLoopView.updateParam(newValue / 1000.0, "tp");
    }
    
    /**
     * ��berpr��ft ob die Werte f��r Tu, Tg und k plausibel sind.
     * Getestet wird:
     *  - Verh��ltnis tu/tg nicht zu gross und nicht zu klein
     *  - k nicht negativ
     *  - entweder PI oder PID selektiert
     *  
     * @param tuValue
     * @param tgValue
     * @param kValue
     * @return
     */
    private boolean validateValues(double tuValue, double tgValue, double kValue, boolean pidState, boolean piState){
    	double v=0;
    	if(tgValue!=0) {
    		v = tuValue / tgValue;
    	}
    	
		if ( v > 0.64173) {
			smartLoopView.updateConsole("Tu/Tg too great --> N would be greater than 8");
			return false;
		}
		else if (v < 0.001) {
			smartLoopView.updateConsole("Tu/Tg too small --> N would be less than 1");
			return false;
		}
		if (kValue < 0) {
			smartLoopView.updateConsole("k must be positive");
			return false;
		}
		if (! pidState ^ piState) {
			smartLoopView.updateConsole("Bitte einen Regelertyp auswählen");
			return false;
		}
		return true;
    }
 
    
    private void addplot(double[] xValues,double[] yValues){
    	plotNummerierung++;
    	XYSeries schrittantwort=new XYSeries("Schrittantwort"+plotNummerierung);
    	for (int i = 0; i < xValues.length; i++) {
    		schrittantwort.add(xValues[i], yValues[i]);    		
		}
    	smartLoopView.updatePlot(schrittantwort);    	
    }
}