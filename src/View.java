import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDatasetTableModel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;




public class View extends JPanel implements ActionListener, ChangeListener, FocusListener {
	static final int initState = 1;
	static final int calculatingState = 2;
	static final int modifyPIDState = 3;
	static final int modifyPIState = 4;
	
	static final String numFormat = "%1$,.3f";
	
	private int state = initState;

	private JPanel 	panel1=new JPanel();
	private JPanel 	panel2=new JPanel();
	private JPanel 	panel3=new JPanel();
	private JPanel	panel3Simple =new JPanel();
	private JPanel 	panel4=new JPanel();
	private JPanel 	panel5=new JPanel();
	private JPanel	panel6 = new JPanel();
	
	
	private ChartPanel chartPanel;
	
	private JLabel lbTu=new JLabel("Tu: ");
	private JEngineeringTextField tfTu =new JEngineeringTextField(0);
	private JLabel lbTg=new JLabel("Tg: ");
	private JEngineeringTextField tfTg =new JEngineeringTextField(0);
	private JLabel lbk=new JLabel("k: ");
	private JEngineeringTextField tfk =new JEngineeringTextField(0);
	
	
	private JToggleButton btPID =new JToggleButton("PID");
	private JToggleButton btPI =new JToggleButton("PI");
	private JButton btClear =new JButton("Clear");
	private JButton btBerechnen =new JButton("Berechnen");
	private ButtonGroup btGroup =new ButtonGroup();

	
	
	private JLabel lbUeberschwingen=new JLabel("Überschwingen");
	private JFormattedDoubleTextField tfOver = new JFormattedDoubleTextField(0);
	
	private JLabel lbKr=new JLabel("Kr: ");
	private JFormattedDoubleTextField tfKr =new JFormattedDoubleTextField(0);
	private JSlider sliderKr =new JSlider(0,100000,50000);
	private JLabel lbTn=new JLabel("Tn: ");
	private JFormattedDoubleTextField tfTn =new JFormattedDoubleTextField(0);
	private JSlider sliderTn =new JSlider(0,100000,50000);
	private JLabel lbTv=new JLabel("Tv: ");
	private JFormattedDoubleTextField tfTv =new JFormattedDoubleTextField(0);
	private JSlider sliderTv =new JSlider(0,100000,50000);
	private JLabel lbTp=new JLabel("Tp: ");
	private JFormattedDoubleTextField tfTp =new JFormattedDoubleTextField(0);
	private JSlider sliderTp =new JSlider(0,100000,50000);
	

	
	private JSlider sliderOpt =new JSlider(0,(int)(1000*Math.PI),(int)(500*Math.PI));
	private JLabel lbOpt=new JLabel("Optimieren");
	
	
	
	
	private JToggleButton btExpert=new JToggleButton("Expert");
	private JToggleButton btSipmle=new JToggleButton("Einfach");
	private ButtonGroup exSi =new ButtonGroup();
	
	
	private JTextField tfKonsole =new JTextField("Konsole...");
	private JPanel panelLegende=new JPanel();
	
    XYSeriesCollection dataset = new XYSeriesCollection();
	
	private Controller actionHandler;
	
	
	public View() {

		super(new GridBagLayout());
		super.setBackground(Color.lightGray);

		btGroup.add(btPI);
		btGroup.add(btPID);
		
		exSi.add(btExpert);
		exSi.add(btSipmle);
		btSipmle.setSelected(true);
		
		btBerechnen.addActionListener(this);
		btClear.addActionListener(this);
		
		tfTu.setMaxValue(100);
		tfTu.setMinValue(0);
		tfTu.setEmptyAllowed(false);
		
		tfTg.setMaxValue(100);
		tfTg.setMinValue(0);
		tfTg.setEmptyAllowed(false);
		
		tfk.setMaxValue(100);
		tfk.setMinValue(0);
		tfk.setEmptyAllowed(false);


		
		/*
		 *
		 *
		 * x, y, x-span, y-span, x-weight, y-weight, anchor, fill, insets(int
		 * top, int left, int bottom, int right), internal padding x, internal
		 * padding y.
		 * 
		 */
	
//*****************************************************************************		

        

        
        JFreeChart chart = ChartFactory.createXYLineChart(" ", "Zeit", "Amplitude", dataset);
 //     chart.removeLegend();
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        
        

//--------------------------------------------------------------------------------------
        
		
		add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 2), 0, 0));
		add(panel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 2), 0, 0));
		add(panel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						2, 0, 2, 2), 0, 0));
		add(panel3Simple, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 2), 0, 0));
		add(panel4, new GridBagConstraints(1, 0, 1, 5, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		add(panel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 2), 0, 0));
		
		panel1.setLayout(new GridBagLayout());
		Insets ins1=new Insets(5,5,5,5);
		
		
		panel1.add(lbTu, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1, 0, 0));
		panel1.add(tfTu, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,ins1, 0, 0));
		panel1.add(lbTg, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1, 0, 0));
		panel1.add(tfTg, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, ins1, 0, 0));
		panel1.add(lbk, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, ins1, 0, 0));
		panel1.add(tfk, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, ins1, 0, 0));
		
		
		tfOver.setMaxValue(100);
		tfOver.setMinValue(0.0005);
		
		btPID.setPreferredSize(new Dimension(50, 25));
		btPI.setPreferredSize(new Dimension(50, 25));
		btClear.setMinimumSize(new Dimension(90, 25));
		btBerechnen.setMinimumSize(new Dimension(90, 25));
		btClear.setPreferredSize(new Dimension(90, 25));
		btBerechnen.setPreferredSize(new Dimension(90, 25));
		
		

		

		panel2.setMinimumSize(new Dimension(200,130));
		panel2.setPreferredSize(new Dimension(200,130));
		
		panel2.setLayout(new GridBagLayout());
		panel2.add(btPID, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(3, 0, 10, 0), 0, 0));
		panel2.add(btPI, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(3, 0, 10, 0), 0, 0));
		panel2.add(lbUeberschwingen, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(3, 10, 0, 0), 0, 0));
		panel2.add(tfOver, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
		panel2.add(btClear, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
		panel2.add(btBerechnen, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
				
		btSipmle.addActionListener(this);
		btExpert.addActionListener(this);
		
		
		sliderKr.setPreferredSize(new Dimension(150, 25));
		sliderKr.setMinimumSize(new Dimension(150, 25));
		sliderTn.setPreferredSize(new Dimension(150, 25));
		sliderTn.setMinimumSize(new Dimension(150, 25));
		sliderTv.setPreferredSize(new Dimension(150, 25));
		sliderTv.setMinimumSize(new Dimension(150, 25));
		sliderTp.setPreferredSize(new Dimension(150, 25));
		sliderTp.setMinimumSize(new Dimension(150, 25));
		
		
		
		
		sliderKr.addChangeListener(this);
		sliderTn.addChangeListener(this);
		sliderTv.addChangeListener(this);
		sliderTp.addChangeListener(this);

		
		panel3.setLayout(new GridBagLayout());
		panel3.add(sliderKr, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
		panel3.add(lbKr, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, ins1, 0, 0));
		panel3.add(tfKr, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,ins1, 0, 0));
		panel3.add(sliderTn, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
		panel3.add(lbTn, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, ins1, 0, 0));
		panel3.add(tfTn, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,ins1, 0, 0));
		panel3.add(sliderTv, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
		panel3.add(lbTv, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, ins1, 0, 0));
		panel3.add(tfTv, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,ins1, 0, 0));
		panel3.add(sliderTp, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 0), 0, 0));
		panel3.add(lbTp, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, ins1, 0, 0));
		panel3.add(tfTp, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,ins1, 0, 0));
		
		
		panel6.setLayout(new GridBagLayout());
		panel6.add(btSipmle, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(3, 0, 3, 0), 0, 0));
		panel6.add(btExpert, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(3, 0, 3, 0), 0, 0));

		sliderOpt.setPreferredSize(new Dimension(150, 25));
		sliderOpt.setMinimumSize(new Dimension(150, 25));
		sliderOpt.addChangeListener(this);
		
		panel3Simple.setLayout(new GridBagLayout());
		panel3Simple.add(lbOpt, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
		panel3Simple.add(sliderOpt, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
		
		
		

		tfKr.addFocusListener(this);
		tfTn.addFocusListener(this);
		tfTv.addFocusListener(this);
		tfTp.addFocusListener(this);
		tfKr.addActionListener(this);
		tfTn.addActionListener(this);
		tfTv.addActionListener(this);
		tfTp.addActionListener(this);
		
		tfKonsole.setEditable(false);
				
		panel4.setLayout(new GridBagLayout());
		panel4.add(chartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//		panel4.add(panelLegende, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
//				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
//==> Legende
		
		panel4.add(tfKonsole, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
	
		panel1.setBackground(Color.white);
		panel2.setBackground(Color.white);
		panel3.setBackground(Color.white);
		panel3Simple.setBackground(Color.white);
		panel4.setBackground(Color.white);
		panel5.setBackground(Color.white);
		panel6.setBackground(Color.white);
		
		setState(initState);
	

		//TODO Demo Werte wieder entfernen----------------------------------------------------------------		
		//Strecke 1: 
		tfTu.setValue(3.08);
		tfTg.setValue(30.8);
		tfk.setValue(0.5);
		btPID.setSelected(true);
		tfOver.setText("20");
		//-----------------------------------------------------------------------------------------------
		
		
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventSource = e.getSource();
		if (eventSource == btBerechnen){
			actionHandler.berechnenPressed(tfTu.getValue(), tfTg.getValue(), tfk.getValue(), btPID.isSelected(), btPI.isSelected(),tfOver.getText());
		} else if (eventSource == btClear){
//			dataset.removeAllSeries();
			actionHandler.clearPressed();
		} else if(eventSource==tfKr||eventSource==tfTn||eventSource==tfTv||eventSource==tfTp){
			double[] eingabe=new double[4];
			eingabe[0]=Double.parseDouble(tfKr.getText());
			eingabe[1]=Double.parseDouble(tfTn.getText());
			eingabe[2]=Double.parseDouble(tfTv.getText());
			eingabe[3]=Double.parseDouble(tfTp.getText());
			actionHandler.tfValuesChanged(eingabe);
		} else if(eventSource==btSipmle)
		{
			remove(panel3);
			add(panel3Simple, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
							0, 0, 0, 2), 0, 0));
			updateUI();		
		}else if(eventSource==btExpert)
		{
			remove(panel3Simple);
			add(panel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
							0, 0, 0, 2), 0, 0));
			updateUI();		
		}		
	}
	
	public void stateChanged(ChangeEvent e){
		JSlider eventSource = (JSlider)e.getSource();
		boolean recalc = ! eventSource.getValueIsAdjusting();
		if (eventSource == sliderKr){			
			actionHandler.paramUpdated(sliderKr.getValue(), "kr", recalc);
		} else if (eventSource == sliderTn) {
			actionHandler.paramUpdated(sliderTn.getValue(), "tn", recalc);
		} else if (eventSource == sliderTv) {
			actionHandler.paramUpdated(sliderTv.getValue(), "tv", recalc);
		} else if (eventSource == sliderTp) {
			actionHandler.paramUpdated(sliderTp.getValue(), "tp", recalc);
		}else if(eventSource== sliderOpt){
			actionHandler.optiSliderUpdated(sliderOpt.getValue());
			//TODO: Wert wiitergää
		}
	}
	
	public void focusLost(FocusEvent e) {
		double[] eingabe=new double[4];
		eingabe[0]=Double.parseDouble(tfKr.getText());
		eingabe[1]=Double.parseDouble(tfTn.getText());
		eingabe[2]=Double.parseDouble(tfTv.getText());
		eingabe[3]=Double.parseDouble(tfTp.getText());
		actionHandler.tfValuesChanged(eingabe);
	}
	
	public void setActionHandler(Controller actionHandler){
		this.actionHandler = actionHandler;
	}
	
	public void updateParam(Double newValue, String param){
		JSlider updateSlider = sliderKr;
		JTextField updateTf = tfKr;
		switch (param){
			case "kr":
				updateSlider = sliderKr;
				updateTf = tfKr;
				break;
			case "tn":
				updateSlider = sliderTn;
				updateTf = tfTn;
				break;
			case "tv":
				updateSlider = sliderTv;
				updateTf = tfTv;
				break;
			case "tp":
				updateSlider = sliderTp;
				updateTf = tfTp;
				break;
		}
		if (updateSlider.getValue() != newValue.intValue()* 1000){
			updateTf.setText(String.format(numFormat, newValue));
			updateSlider.setValue((int)(newValue * 1000.0));
		}
	}	
	
	public void updateSliderMaxValues(double[] MaxValues){
		sliderKr.setMaximum((int)(MaxValues[0]*2000.0));
		sliderTn.setMaximum((int)(MaxValues[1]*2000.0));
		sliderTv.setMaximum((int)(MaxValues[2]*2000.0));
		sliderTp.setMaximum((int)(MaxValues[3]*2000.0));
	}
	
	public void updateConsole(String text){
		tfKonsole.setText(text);
	}
	
	public void addPlot(XYSeries data){
		if(dataset.getSeriesIndex(data.getKey())==-1){
			dataset.addSeries(data);
			chartPanel.repaint();
			chartPanel.restoreAutoBounds(); //Zoom zurücksetzen;
		}
		else{
			System.out.println("Plot mit dem selben 'Key' schon vorhanden");
		}
	}
	
	public void removePlot(XYSeries data){
		dataset.removeSeries(dataset.getSeries(data.getKey()));
	}
	
	public void removeAllPlots(){
		System.out.println("view addPlot");
		dataset.removeAllSeries();
	}
	
	//TODO Syso's entfernen...
	 
	public void updatePlot(XYSeries data){	
		System.out.println("view updatePlot");
		if(dataset.getSeriesIndex(data.getKey())==-1){
			addPlot(data);
			System.out.println("Plot nicht vorhanden neuser Plot wird erstellt");
		}else{
			System.out.println("Plot ersetzen..");
			dataset.removeSeries(dataset.getSeries(data.getKey()));
			dataset.addSeries(data);
			System.out.println("Plot update done");
		}
	}
	
	
	public int getState(){
		return state;
	}
	
	public double[] getParamValues(){
		double [] paramValues = new double[4];
		paramValues[0] = Double.parseDouble(tfKr.getText());
		paramValues[1] = Double.parseDouble(tfTn.getText());
		paramValues[2] = Double.parseDouble(tfTv.getText());
		paramValues[3] = Double.parseDouble(tfTp.getText());
		
		return paramValues;
	}
	
	
	public void setState(int newState){
		state = newState;
		switch (state){
			case initState:
				tfTu.setEnabled(true);
				tfTg.setEnabled(true);
				tfk.setEnabled(true);
				tfOver.setEnabled(true);
				btClear.setEnabled(false);
				btBerechnen.setEnabled(true);
				btPID.setEnabled(true);
				btPI.setEnabled(true);
				sliderKr.setEnabled(false);
				sliderTn.setEnabled(false);
				sliderTv.setEnabled(false);
				sliderTp.setEnabled(false);
				tfKr.setEnabled(false);
				tfTn.setEnabled(false);
				tfTv.setEnabled(false);
				tfTp.setEnabled(false);
				break;
				
			case calculatingState:
				tfTu.setEnabled(false);
				tfTg.setEnabled(false);
				tfk.setEnabled(false);
				tfOver.setEnabled(false);
				btClear.setEnabled(true);
				btBerechnen.setEnabled(false);
				btPID.setEnabled(false);
				btPI.setEnabled(false);
				sliderKr.setEnabled(false);
				sliderTn.setEnabled(false);
				sliderTv.setEnabled(false);
				sliderTp.setEnabled(false);
				tfKr.setEnabled(false);
				tfTv.setEnabled(false);
				tfTn.setEnabled(false);
				tfTp.setEnabled(false);
				break;
				
			case modifyPIDState:
				tfTu.setEnabled(false);
				tfTg.setEnabled(false);
				tfk.setEnabled(false);
				tfOver.setEnabled(false);
				btClear.setEnabled(true);
				btBerechnen.setEnabled(false);
				btPID.setEnabled(false);
				btPI.setEnabled(false);
				sliderKr.setEnabled(true);
				sliderTn.setEnabled(true);
				sliderTv.setEnabled(true);
				sliderTp.setEnabled(true);
				tfKr.setEnabled(true);
				tfTn.setEnabled(true);
				tfTv.setEnabled(true);
				tfTp.setEnabled(true);
				break;
				
			case modifyPIState:
				tfTu.setEnabled(false);
				tfTg.setEnabled(false);
				tfk.setEnabled(false);
				tfOver.setEnabled(false);
				btClear.setEnabled(true);
				btBerechnen.setEnabled(false);
				btPID.setEnabled(false);
				btPI.setEnabled(false);
				sliderKr.setEnabled(true);
				sliderTn.setEnabled(true);
				sliderTv.setEnabled(false);
				sliderTp.setEnabled(false);
				tfKr.setEnabled(true);
				tfTn.setEnabled(true);
				tfTv.setEnabled(false);
				tfTp.setEnabled(false);
				break;
		}
	}



	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

}
