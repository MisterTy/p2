package Controller;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColorCellRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private PlotManager plotManager;
	
	public ColorCellRenderer(PlotManager manager){
		plotManager = manager;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setOpaque(true);
		setBackground(plotManager.getCellColor(row));
		return this;
	}

}