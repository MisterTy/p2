package Aux;

import Model.Regelkreis;

public class Notification {
	public final static int newRegelkreis = 0;
	public final static int updatedRegelkreis = 1;
	public final static int updatedStepResponse = 2;
	public final static int deletedRegelkreis = 3;
	
	private DimensioningResult dimRes;
	private Regelkreis regelkreis;
	private int message;
	
	public Notification(int notificationType){
		message = notificationType;
	}
	
	public void setDimensioningResult(DimensioningResult dimensioningResult){
		dimRes = dimensioningResult;
	}
	public void setRegelkreis(Regelkreis regelkreis){
		this.regelkreis = regelkreis;
	}
	
	public int getMessage(){
		return message;
	}
	public DimensioningResult getDimensioningResult(){
		return dimRes;
	}
	public Regelkreis getRegelkreis(){
		return regelkreis;
	}

}
