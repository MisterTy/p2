package Controller;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.data.xy.XYSeries;

import Auxillary.DimensioningResult;
import Auxillary.Notification;
import Model.Model;
import Model.Regelkreis;
import View.View;
import View.Console;

/**
 * Verwaltet eingaben, die druch das View gemacht werden.
 */
public class Controller implements Runnable, Observer {
	Model smartLoopModel;
	View smartLoopView;
	TableModel stepResponseTableModel;
	TableModel streckenParamTableModel;
	TableModel reglerParamTableModel;
	
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
		 frame.setMinimumSize(new Dimension(1200, 560));
		 frame.setVisible(true);
    }
    
    public void startGui(){
    	SwingUtilities.invokeLater(this);
    }
    
    
    public void berechnenPressed(double tuValue, double tgValue, double kValue, boolean pidState, boolean piState, String uberschwingen){
    	if (validateValues(tuValue, tgValue, kValue, pidState, piState)){
    		System.out.println("before addPlot");
    		//addPlot();
    		System.out.println("after addPlot");
    		smartLoopView.setState(View.calculatingState);
    		//smartLoopView.updateConsole("Berechnung gestartet");
    		int type = Model.pidRegler;
    		if (piState){
    			type = Model.piRegler;
    		}
    		
    		// Start Calculations
    		smartLoopModel.setAnzahlPunkte(1000);
    		smartLoopModel.setStrecke(tuValue, tgValue);
    		smartLoopModel.setUberschwingen(Double.parseDouble(uberschwingen));;
    		smartLoopModel.setVerstarkung(kValue);
    		smartLoopModel.addRegelkreis(type);	
    	}
    	else{
    		smartLoopView.invalidateInputFields();
    		System.out.println("Somthing is wrong with entered values...");
    	}
    }
    
    public void update(Observable sender, Object args){
    	System.out.println("update called...............");
    	
    	Notification note = (Notification) args;
    	Regelkreis regelkreis;
    	DimensioningResult dimRes;
    	switch (note.getMessage()){
    	case Notification.newRegelkreis:
    		regelkreis = note.getRegelkreis();
    		dimRes = note.getDimensioningResult();
    		handleNewRegelkreis(regelkreis, dimRes);
    		smartLoopView.updateConsole("Neuer Regelkreis berechnet", Console.success);
    		break;
    	case Notification.updatedRegelkreis: case Notification.updatedStepResponse:
    		regelkreis = note.getRegelkreis();
    		handleUpdatedRegelkreis(regelkreis);
    		break;
    	}
    }
    
    private void handleNewRegelkreis(Regelkreis regelkreis, DimensioningResult dimRes){
    	initComplete = false;
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
    	smartLoopView.addPlot(regelkreis);
    	//updatePlot(smartLoopModel.getXValues(),smartLoopModel.getYValues());
    	initComplete = true;
    }
    
    private void handleUpdatedRegelkreis(Regelkreis regelkreis){
    	smartLoopView.updatePlot(regelkreis);
    }
    
    public void clearPressed(){
    	//smartLoopModel.removeRegelkreis(0);
    	smartLoopView.setState(View.initState);
    	smartLoopView.updateConsole("Neue Werte können eingegeben werden...");
    }
    
    public void deleteRegelkreis(int index){
    	smartLoopModel.removeRegelkreis(index);
    }
    
    public void deleteAllRegelkreise(){
    	smartLoopModel.deleteAllRegelkreise();
    }
    
    public void paramUpdated(int newValue, String param, int rkIndex, boolean recalc){
    	smartLoopView.updateParam((float)newValue / 1000.0, param);
    	if (initComplete) { //recalc && initComplete
    		System.out.println("==========Starting new SR Calculation=========");
    		smartLoopModel.updateStepResponse(rkIndex, smartLoopView.getParamValues());
    		//updatePlot(smartLoopModel.getXValues(), smartLoopModel.getYValues());
    	}
    }
    
    public void optiSliderUpdated(int newValue, int regelkreisIndex){
    	smartLoopModel.updateRegelkreis(((newValue / 1000.0) - Math.PI/2), regelkreisIndex);
    }
    

    private void krUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "kr");
    }

    private void tnUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "tn");
    }

    private void tvUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "tv");
    }
    
    private void tpUpdated(int newValue){
    	smartLoopView.updateParam((float)newValue / 1000.0, "tp");
    }
    
    public void tfValuesChanged(double[] tfValues){
    	smartLoopView.updateSliderMaxValues(tfValues);   
    	
    	krUpdated((int)(tfValues[0] * 1000.0));
    	tnUpdated((int)(tfValues[1] * 1000.0));
    	if(smartLoopView.getState() == View.modifyPIDState){
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
			smartLoopView.updateConsole("Tu/Tg too great --> N would be greater than 8", Console.error);
			return false;
		}
		else if (v < 0.001) {
			smartLoopView.updateConsole("Tu/Tg too small --> N would be less than 1", Console.error);
			return false;
		}
		if (kValue < 0) {
			smartLoopView.updateConsole("k must be positive", Console.error);
			return false;
		}
		if (! pidState ^ piState) {
			smartLoopView.updateConsole("Bitte einen Regelertyp auswählen", Console.error);
			return false;
		}
		return true;
    }
}