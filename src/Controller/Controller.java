package Controller;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
    
    
    public void berechnenPressed(double tuValue, double tgValue, double kValue, boolean pidState, boolean piState, double uberschwingen){
    	if (validateValues(tuValue, tgValue, kValue, pidState, piState)){
    		smartLoopView.setState(View.calculatingState);
    		int type = Model.pidRegler;
    		if (piState){
    			type = Model.piRegler;
    		}
    		
    		// Start Calculations
    		smartLoopModel.setAnzahlPunkte(1000);
    		smartLoopModel.setStrecke(tuValue, tgValue);
    		smartLoopModel.setUberschwingen(uberschwingen);
    		smartLoopModel.setVerstarkung(kValue);
    		smartLoopModel.addRegelkreis(type);	
    	}
    	else{
    		smartLoopView.invalidateInputFields();
    	}
    }
    
    public void update(Observable sender, Object args){
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
    	case Notification.updatedRegelkreis:
    		regelkreis = note.getRegelkreis();
    		smartLoopView.updatePlot(regelkreis);
    		smartLoopView.updateOptiSlider(regelkreis.getKkfRaw());
        	checkSanity(regelkreis);
        	updateParamValues(regelkreis.getResult().getParamArray());
    		break;
    	case Notification.updatedStepResponse:
    		regelkreis = note.getRegelkreis();
    		checkSanity(regelkreis);
    		smartLoopView.updatePlot(regelkreis);
    		break;
    	}
    }
    
    private void handleNewRegelkreis(Regelkreis regelkreis, DimensioningResult dimRes){
    	initComplete = false;
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
    	initComplete = true;
    }
    
    public void clearPressed(){
    	smartLoopView.setState(View.initState);
    	smartLoopView.updateConsole("Neue Werte können eingegeben werden...");
    }
    
    public void deleteRegelkreis(int index){
    	smartLoopModel.removeRegelkreis(index);
    	if (smartLoopModel.getAnzRegelkreise() == 0){
    		smartLoopView.setState(View.initState);
    	}
    }
    
    public void deleteAllRegelkreise(){
    	smartLoopModel.deleteAllRegelkreise();
    	smartLoopView.setState(View.initState);
    }
    
    public void paramUpdated(int newValue, String param, int rkIndex){
    	smartLoopView.updateParam((float)newValue / 1000.0, param);
    	if (initComplete) {
    		smartLoopModel.updateStepResponse(rkIndex, smartLoopView.getParamValues());
    	}
    }
    
    public void optiSliderUpdated(int newValue, int regelkreisIndex){
    	double kkf = (newValue / 1000.0) - Math.PI/2;
    	if (smartLoopModel.updateNecessary(regelkreisIndex, kkf)){
    		smartLoopModel.updateRegelkreis(kkf, regelkreisIndex);
    	}
    }
    
    public void updateParamValues(double[] paramValues){
    	initComplete = false;
    	
    	smartLoopView.updateParam(paramValues[0], "kr");
    	smartLoopView.updateParam(paramValues[1], "tn");
    	if (smartLoopView.getState() == View.modifyPIDState){
    		smartLoopView.updateParam(paramValues[2], "tv");
        	smartLoopView.updateParam(paramValues[3], "tp");
    	}
    	initComplete = true;
    }
    
    public void tfValuesChanged(double[] tfValues, int rkIndex){
    	if (initComplete) {
    		System.out.println(Arrays.toString(tfValues));
    		updateParamValues(tfValues);
    		smartLoopView.updateSliderMaxValues(tfValues);
    		smartLoopModel.updateStepResponse(rkIndex, tfValues);
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
    
    private void checkSanity(Regelkreis regelkreis){
    	boolean insane = false;
    	double[] paramValues = regelkreis.getResult().getParamArray();
    	for (int i=0; i<paramValues.length; i++){
    		if (paramValues[i]<0 || paramValues[i]>100){
    			insane = true;
    		}
    	}
    	if (regelkreis.getOvershoot() < 0 || regelkreis.getOvershoot() > 100){
    		insane = true;
    	}
    	if (insane){
    		smartLoopView.updateConsole("Warnung: Regelkreis ist möglicherweise instabil", Console.warning);
    	} else{
    		smartLoopView.clearConsole();
    	}
    }
}