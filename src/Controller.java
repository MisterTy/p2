
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.data.xy.XYSeries;
/**
 * Verwaltet eingaben, die druch das View gemacht werden.
 */
public class Controller implements Runnable, Observer {
	Model smartLoopModel;
	View smartLoopView;
	
	boolean initComplete = false;
	
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
    		addPlot();
    		smartLoopView.setState(View.calculatingState);
    		smartLoopView.updateConsole("Berechnung gestartet");
    		
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
    	}
    	else{
    		System.out.println("Somthing is wrong with entered values...");
    	}
    }
    
    public void update(Observable sender, Object args){
    	System.out.println("update called...............");
    	initComplete = false;
    	smartLoopView.updateConsole("Berechnete Werte werden ausgegeben");
    	
    	DimensioningResult dimRes = (DimensioningResult) args;
    	smartLoopModel.output();
    	if (dimRes.getType() == Model.piRegler){
			smartLoopView.setState(View.modifyPIState);
			smartLoopView.updateParam(dimRes.getKr(), "kr");
			smartLoopView.updateParam(dimRes.getTn(), "tn");
			smartLoopView.updateParam(0.0, "tv");
			smartLoopView.updateParam(0.0, "tp");
		} else {
			smartLoopView.setState(View.modifyPIDState);
			smartLoopView.updateParam(dimRes.getKr(), "kr");
			smartLoopView.updateParam(dimRes.getTn(), "tn");
			smartLoopView.updateParam(dimRes.getTv(), "tv");
			smartLoopView.updateParam(dimRes.getTp(), "tp");
		}
    	
    	smartLoopView.updateSliderMaxValues(dimRes.getParamArray());
    	updatePlot(smartLoopModel.getXValues(),smartLoopModel.getYValues());
    	initComplete = true;
    }
    
    public void clearPressed(){
    	smartLoopModel.removeRegelkreis();
    	smartLoopView.setState(View.initState);
    	smartLoopView.updateConsole("Neue Werte können eingegeben werden...");
    }
    
    public void paramUpdated(int newValue, String param, boolean recalc){
    	smartLoopView.updateParam((float)newValue / 1000.0, param);
    	if (initComplete) { //recalc && initComplete
    		System.out.println("==========Starting new SR Calculation=========");
    		smartLoopModel.updateStepResponse(0, smartLoopView.getParamValues());
    		updatePlot(smartLoopModel.getXValues(), smartLoopModel.getYValues());
    	}
    }
    
	// DEPRECIATED - DO NOT USE ANYMORE
    public void krUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "kr");
    }
    // DEPRECIATED - DO NOT USE ANYMORE
    public void tnUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "tn");
    }
    // DEPRECIATED - DO NOT USE ANYMORE
    public void tvUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "tv");
    }
    // DEPRECIATED - DO NOT USE ANYMORE
    public void tpUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "tp");
    }
    
    public void tfValuesChanged(double[] tfValues){
    	smartLoopView.updateSliderMaxValues(tfValues);   
    	
    	krUpdated((int)(tfValues[0] * 1000.0));
    	tnUpdated((int)(tfValues[1] * 1000.0));
    	if(smartLoopView.getState() == smartLoopView.modifyPIDState){
    		tvUpdated((int)(tfValues[2] * 1000.0));
			tpUpdated((int)(tfValues[3] * 1000.0));
    	}    		
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
 
    private void updatePlot(double[] xValues, double[] yValues){
    	System.out.println("ctrl updatePlot");
    	//System.out.println("X Werte: "+Arrays.toString(xValues));
    	//System.out.println("Y Werte: "+Arrays.toString(yValues));
    	XYSeries schrittantwort=new XYSeries("Schrittantwort "+(plotNummerierung));
    	for (int i = 0; i < xValues.length; i++) {
    		schrittantwort.add(xValues[i], yValues[i]);    		
		}
    	smartLoopView.updatePlot(schrittantwort);
    }
    
    private void addPlot(){
    	plotNummerierung++;
    	System.out.println("ctrl addPlot"); /*
    	XYSeries schrittantwort=null;
    	System.out.println("adding Plot");
    	plotNummerierung++;
    	schrittantwort=new XYSeries("Schrittantwort "+plotNummerierung);   	
    	for (int i = 0; i < xValues.length; i++) {
    		schrittantwort.add(xValues[i], yValues[i]);    		
		}
    	smartLoopView.addPlot(schrittantwort);    	*/
    }
}