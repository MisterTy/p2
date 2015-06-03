package Controller;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;

import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Model.Regelkreis;


public class PlotManager{
	public static final int stepResponseTable = 0;
	public static final int streckenInfoTable = 1;
	public static final int reglerInfoTable = 2;
	
	private TableModel stepResponseTableModel = new TableModel(stepResponseTable);
	private TableModel streckenInfoTableModel = new TableModel(streckenInfoTable);
	private TableModel reglerInfoTableModel = new TableModel(reglerInfoTable);
	
	private XYItemRenderer renderer;
	private XYSeriesCollection dataset = new XYSeriesCollection();
	
	private int idCounter;
	private ArrayList<Plot> plotList = new ArrayList<Plot>();
	
	private int selectedIndex = 0;
	
	// ---------- Getters ----------
	
	public TableModel getTableModel(int tableType){
		switch (tableType){
		case stepResponseTable:
			return stepResponseTableModel;
		case streckenInfoTable:
			return streckenInfoTableModel;
		case reglerInfoTable:
			return reglerInfoTableModel;
		default:
			return new TableModel(-1);
		}
	}
	
	public XYSeriesCollection getDataset(){
		return dataset;
	}
	
	public Plot getSelectedPlot(){
		return plotList.get(selectedIndex);
	}
	
	
	// ---------- Setters ----------
	
	public void setRenderer(XYItemRenderer renderer){
		this.renderer = renderer;
	}
	
	// ---------- Management Logic ----------
	
	public int addPlot(Regelkreis regelkreis){
		Plot plot = new Plot();
		plot.regelkreis = regelkreis;
		plot.id = ++idCounter;
		plot.name = "Regelkreis "+plot.id;
		XYSeries data = new XYSeries(plot.id);
		double[] xValues = regelkreis.getXValues();
		double[] yValues = regelkreis.getYValues();
		for (int i=0; i<xValues.length; i++){
			data.add(xValues[i], yValues[i]);
		}
		plot.data = data;
		
		dataset.addSeries(data);
		plot.seriesIndex = lookupSeriesIndex(plot.data);
		plot.lineColor = lookupLineColor(plot.seriesIndex);
		
		plotList.add(plot);
		stepResponseTableModel.addItem(plot);
		stepResponseTableModel.fireTableDataChanged();
		return plotList.indexOf(plot);
	}
	
	public int removeSelectedPlot(){
		Plot selectedPlot = plotList.get(selectedIndex);
		dataset.removeSeries(selectedPlot.data);
		stepResponseTableModel.removeItem(selectedIndex);
		stepResponseTableModel.fireTableDataChanged();
		plotList.remove(selectedIndex);
		
		for (int i=0; i<plotList.size(); i++){
			Plot tmpPlot = plotList.get(i);
			tmpPlot.seriesIndex = lookupSeriesIndex(tmpPlot.data);
			tmpPlot.lineColor = lookupLineColor(tmpPlot.seriesIndex);
			plotList.set(i, tmpPlot);
		}
		
		if (plotList.size() == 0){
			selectedIndex = -1;
			streckenInfoTableModel.setSelectedPlot(null);
			reglerInfoTableModel.setSelectedPlot(null);
		} else if (selectedIndex != 0) {
			selectedIndex -= 1;
		}
		return selectedIndex;
	}
	
	public void removeAllPlots(){
		plotList.clear();
		stepResponseTableModel.purge();
		streckenInfoTableModel.purge();
		reglerInfoTableModel.purge();
		stepResponseTableModel.fireTableDataChanged();
		streckenInfoTableModel.fireTableDataChanged();
		reglerInfoTableModel.fireTableDataChanged();
		dataset.removeAllSeries();
	}
	
	public void updateSelectedPlot(Regelkreis regelkreis){
		Plot selectedPlot = plotList.get(selectedIndex);
		Plot newPlot = new Plot();
		newPlot.regelkreis = regelkreis;
		newPlot.id = selectedPlot.id;
		newPlot.name = selectedPlot.name;
		double[] xValues = regelkreis.getXValues();
		double[] yValues = regelkreis.getYValues();
		for (int i=0; i<xValues.length; i++){
			selectedPlot.data.updateByIndex(i, yValues[i]);
		}
		newPlot.data = selectedPlot.data;

		newPlot.seriesIndex = lookupSeriesIndex(newPlot.data);
		newPlot.lineColor = lookupLineColor(newPlot.seriesIndex);
		plotList.remove(selectedIndex);
		plotList.add(selectedIndex, newPlot);
		stepResponseTableModel.updateItem(newPlot, selectedIndex);
		stepResponseTableModel.fireTableDataChanged();
		reglerInfoTableModel.fireTableDataChanged();
	}
	
	public Color getCellColor(int row){
		return plotList.get(row).lineColor;
	}
	
	public int getSelectedIndex(){
		return selectedIndex;
	}
	
	// ---------- Listener Methods ----------
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()){
			if (e.getFirstIndex() == selectedIndex){
				selectedIndex = e.getLastIndex();
			} else if (e.getLastIndex() == selectedIndex){
				selectedIndex = e.getFirstIndex();
			} else if (e.getFirstIndex() == e.getLastIndex()){
				selectedIndex = e.getFirstIndex();
			}
			Plot selectedPlot = plotList.get(selectedIndex);
			streckenInfoTableModel.setSelectedPlot(selectedPlot);
			reglerInfoTableModel.setSelectedPlot(selectedPlot);
			streckenInfoTableModel.fireTableDataChanged();
			reglerInfoTableModel.fireTableDataChanged();
		}
	}
	
	private Color lookupLineColor(int seriesIndex){
		return (Color)renderer.getItemPaint(seriesIndex, 0);
	}
	private int lookupSeriesIndex(XYSeries series){
		return dataset.getSeriesIndex(series.getKey());
	}
}
