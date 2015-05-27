package Controller;
import java.awt.Color;

import org.jfree.data.xy.XYSeries;

import Model.Regelkreis;


public class Plot {
	public int id;
	public int seriesIndex;
	public String name;
	public Regelkreis regelkreis;
	public XYSeries data;
	public Color lineColor;
}
