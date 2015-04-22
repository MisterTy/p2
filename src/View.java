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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDatasetTableModel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;




public class View extends JPanel {

	private JPanel 	panel1=new JPanel();
	private JPanel 	panel2=new JPanel();
	private JPanel 	panel3=new JPanel();
	private JPanel 	panel4=new JPanel();
	private JPanel 	panel5=new JPanel();
	
	
	private JLabel lbTu=new JLabel("Tu: ");
	private JEngineeringTextField tfTu =new JEngineeringTextField(0);
	private JLabel lbTg=new JLabel("Tg: ");
	private JTextField tfTg =new JTextField("");
	private JLabel lbk=new JLabel("k: ");
	private JTextField tfk =new JTextField("");
	
	
	private JToggleButton btPID =new JToggleButton("PID");
	private JToggleButton btPI =new JToggleButton("PI");
	private JButton btClear =new JButton("Clear");
	private JButton btBerechnen =new JButton("Berechnen");
	private ButtonGroup btGroup =new ButtonGroup();

	
	
	private JLabel lbUeberschwingen=new JLabel("Überschwingen");
	private JLabel lbWenig=new JLabel("aperiodisch");
	private JLabel lbViel=new JLabel("viel");
	private JSlider slider = new JSlider(0,3,0);
	
	private JLabel lbKr=new JLabel("Kr: ");
	private JTextField tfKr =new JTextField("");
	private JSlider sliderKr =new JSlider(0,100,50);
	private JLabel lbTn=new JLabel("Tn: ");
	private JTextField tfTn =new JTextField("");
	private JSlider sliderTn =new JSlider(0,100,50);
	private JLabel lbTv=new JLabel("Tv: ");
	private JTextField tfTv =new JTextField("");
	private JSlider sliderTv =new JSlider(0,100,50);
	
	
	private JTextField tfKonsole =new JTextField("Konsole...");
	private JPanel panelLegende=new JPanel();
	

	
	public View() {
		
		
		
		super(new GridBagLayout());
		super.setBackground(Color.lightGray);

		btGroup.add(btPI);
		btGroup.add(btPID);
		
		tfTu.setMaxValue(100);
		tfTu.setMinValue(0);
		tfTu.setEmptyAllowed(true);


		
		/*
		 *
		 *
		 * x, y, x-span, y-span, x-weight, y-weight, anchor, fill, insets(int
		 * top, int left, int bottom, int right), internal padding x, internal
		 * padding y.
		 * 
		 */
	
//*****************************************************************************		

        
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        XYSeries leer= new XYSeries("");
        
        
        
        
        XYSeries sinus = new XYSeries("SINUS");

     
        for(double i=0; i<4*Math.PI+0.1 ;i=i+0.1){
        	sinus.add(i,Math.sin(i));
        }
             
        dataset.addSeries(leer);
        
        JFreeChart chart = ChartFactory.createXYLineChart("Test", "X", "Y", dataset);
 //     chart.removeLegend();
        
        ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setMouseWheelEnabled(true);
      

//--------------------------------------------------------------------------------------
        
		
		add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 2), 0, 0));
		add(panel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
						0, 0, 0, 2), 0, 0));
		add(panel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						2, 0, 0, 2), 0, 0));
		add(panel4, new GridBagConstraints(1, 0, 1, 4, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 2, 0, 0), 0, 0));
		add(panel5, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
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
		
		
		slider.setPreferredSize(new Dimension(150, 25));
		slider.setMinimumSize(new Dimension(150, 25));
		
		btPID.setPreferredSize(new Dimension(50, 25));
		btPI.setPreferredSize(new Dimension(50, 25));
		btClear.setMinimumSize(new Dimension(90, 25));
		btBerechnen.setMinimumSize(new Dimension(90, 25));
		btClear.setPreferredSize(new Dimension(90, 25));
		btBerechnen.setPreferredSize(new Dimension(90, 25));

		

		panel2.setMinimumSize(new Dimension(200,160));
		panel2.setPreferredSize(new Dimension(200,160));
		
		panel2.setLayout(new GridBagLayout());
		panel2.add(btPID, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(3, 0, 10, 0), 0, 0));
		panel2.add(btPI, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(3, 0, 10, 0), 0, 0));
		panel2.add(lbUeberschwingen, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));
		panel2.add(slider, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 25, 0, 25), 0, 0));
		panel2.add(lbWenig, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		panel2.add(lbViel, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
		panel2.add(btClear, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(3, 0, 10, 0), 0, 0));
		panel2.add(btBerechnen, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(3, 0, 10, 0), 0, 0));

		sliderKr.setPreferredSize(new Dimension(150, 25));
		sliderKr.setMinimumSize(new Dimension(150, 25));
		sliderTn.setPreferredSize(new Dimension(150, 25));
		sliderTn.setMinimumSize(new Dimension(150, 25));
		sliderTv.setPreferredSize(new Dimension(150, 25));
		sliderTv.setMinimumSize(new Dimension(150, 25));
		

		
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

		
		
		tfKonsole.setEditable(false);
				
		panel4.setLayout(new GridBagLayout());
		panel4.add(chartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//		panel4.add(panelLegende, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
//				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
//==> Legende
		
		panel4.add(tfKonsole, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		

		
		

		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(1);
		
	
		panel1.setBackground(Color.white);
		panel2.setBackground(Color.white);
		panel3.setBackground(Color.white);
		panel4.setBackground(Color.white);
		panel5.setBackground(Color.white);
		
		
		


		
		
		
		
	}
}
