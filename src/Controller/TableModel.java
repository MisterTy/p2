package Controller;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Auxillary.MathLibrary;
import Model.Model;
import View.View;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private int typ = 0;
	
	ArrayList<Plot> plots = new ArrayList<Plot>();
	Plot selectedPlot;
	
	public TableModel(int tableTyp){
		typ = tableTyp;
	}
	
	
	public void addItem(Plot plot){
		plots.add(plot);
	}
	public void removeItem(int itemIndex){
		plots.remove(itemIndex);
	}
	public void purge(){
		plots.clear();
		selectedPlot = null;
	}
	
	public void updateItem(Plot newPlot, int index){
		plots.set(index, newPlot);
		selectedPlot = plots.get(index);
	}
	
	public void setSelectedPlot(Plot selectedPlot){
		this.selectedPlot = selectedPlot;
	}

	@Override
	public int getRowCount() {
		switch(typ){
		case PlotManager.stepResponseTable:
			return plots.size();
		case PlotManager.streckenInfoTable:
			if (selectedPlot == null){
				return 0;
			}
			return 3+selectedPlot.regelkreis.getStrecke().getN();
		case PlotManager.reglerInfoTable:
			if (selectedPlot == null) {
				return 0;
			}
			if (selectedPlot.regelkreis.getTyp() == Model.pidRegler){
				return 7;
			} else if (selectedPlot.regelkreis.getTyp() == Model.piRegler) {
				return 5;
			} else {
				return 0;
			}
		default:
			return 0;
		}
	}

	@Override
	public int getColumnCount() {
		if (typ == PlotManager.stepResponseTable){
			return 2;
		}
		return 2;
	}
	
	@Override
	public String getColumnName(int column) {
		switch (typ) {
			case PlotManager.stepResponseTable:
				if (column == 1){
					return "Name";
				}
				return "";
			case PlotManager.streckenInfoTable: case PlotManager.reglerInfoTable:
				switch (column) {
					case 0:
						return "Parameter";
					case 1:
						return "Wert";
					default:
						return "error";
				}
			default:
				return "error";	
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(typ){
		case PlotManager.stepResponseTable:
			if (columnIndex == 1){
				return plots.get(rowIndex).name;
			}
			return " ";
			
		case PlotManager.streckenInfoTable:
			if (selectedPlot != null){
				if (columnIndex == 0){
					return getStreckenInfoValues(rowIndex, true);
				}
				return getStreckenInfoValues(rowIndex, false);
			} else {
				return null;
			}
		case PlotManager.reglerInfoTable:
			if (selectedPlot != null){
				if (columnIndex == 0){
					return getReglerInfoValues(rowIndex, true);
				}
				return getReglerInfoValues(rowIndex, false);
			} else {
				return null;
			}
		default:
			return "";
		}
	}
	
	private String getStreckenInfoValues(int row, boolean heading){
		if (row == 0){
			if (heading){
				return "Tu";
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getStrecke().getTu());
		} else if (row == 1){
			if (heading){
				return "Tg";
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getStrecke().getTg());
		} else if (row ==2){
			if (heading) {
				return "Ordnung";
			}
			return String.valueOf(selectedPlot.regelkreis.getStrecke().getN());
		} else {
			if (heading){
				return "T"+(row-2);
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getStrecke().getCoeffitients()[row-3]);
		}
	}
	
	private String getReglerInfoValues(int row, boolean heading){
		if (row == 0){
			if (heading){
				return "Typ";
			}
			if (selectedPlot.regelkreis.getTyp() == Model.pidRegler){
				return "PID-Regler";
			} else if (selectedPlot.regelkreis.getTyp() == Model.piRegler){
				return "PI-Regler";
			}
			return "unbekannt";
		} else if (row == 1) {
			if(heading){
				return "Ü'schwingen gewünscht";
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getWantedOvershoot())+"%";
		}else if (row == 2){
			if (heading){
				return "Ü'schwingen erreicht";
			}
			if(selectedPlot.regelkreis.getOvershoot()==0){
				return "Aperiodisch";
			}else{
				return MathLibrary.scientificFormat(selectedPlot.regelkreis.getOvershoot())+"%";
			}
		} else if (row == 3){
			if (heading){
				return "Kr";
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getResult().getKr());
		} else if (row == 4){
			if (heading){
				return "Tn";
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getResult().getTn());
		} else if (row == 5){
			if (heading) {
				return "Tv";
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getResult().getTv());
		} else if (row == 6){
			if (heading) {
				return "Tp";
			}
			return MathLibrary.scientificFormat(selectedPlot.regelkreis.getResult().getTp());
		} else {
			return "error";
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int collumn) {
	    return typ == PlotManager.stepResponseTable;
	  }

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	   {
		Plot changedPlot = plots.get(rowIndex);
		changedPlot.name = (String) aValue;
		updateItem(changedPlot, rowIndex);
	   }

}
