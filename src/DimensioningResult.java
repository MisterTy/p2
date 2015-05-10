
public class DimensioningResult {
	private double kr;
	private double tv;
	private double tn;
	private double tp;
	private int type;
	private double[]paramArray = new double[4];
	
	
	public void setValues(int type, double kr, double tn, double tv, double tp) {
		this.type = type;
		this.kr = kr;
		this.tn = tn;
		this.tv = tv;
		this.tp = tp;
		
		paramArray[0] = kr;
		paramArray[1] = tn;
		paramArray[2] = tv;
		paramArray[3] = tp;
	}
	
	public int getType() {
		return type;
	}
	public double getKr() {
		return kr;
	}
	public double getTn() {
		return tn;
	}
	public double getTv() {
		return tv;
	}
	public double getTp() {
		return tp;
	}
	public double[] getParamArray() {
		return paramArray;
	}

}
