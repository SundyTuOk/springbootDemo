package com.sf.bean;

public class IndexPageMeter {
	
	// ID、Area_ID、User_ID、Meter_ID
	private Integer ID;
	private Integer areaID;
	private String userID;
	private Integer meterID;
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public Integer getAreaID() {
		return areaID;
	}
	public void setAreaID(Integer areaID) {
		this.areaID = areaID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public Integer getMeterID() {
		return meterID;
	}
	public void setMeterID(Integer meterID) {
		this.meterID = meterID;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(!(arg0 instanceof IndexPageMeter)){
			return false;
		}
		IndexPageMeter indexPageMeter = (IndexPageMeter) arg0;
		// 判断areaID
		if(this.areaID != null && indexPageMeter.getAreaID() != null &&
				this.areaID.intValue() != indexPageMeter.getAreaID().intValue()){
			return false;
		}
		if((this.areaID == null && indexPageMeter.getAreaID() != null) ||
				(this.areaID != null && indexPageMeter.getAreaID() == null)){
			return false;
		}
		
		// 判断userID
		if(this.userID != null && indexPageMeter.getUserID() != null &&
				!this.userID.equals(indexPageMeter.getUserID())){
			return false;
		}
		if((this.userID == null && indexPageMeter.getUserID() != null) ||
				(this.userID != null && indexPageMeter.getUserID() == null)){
			return false;
		}
		
		// 判断meterID
		if(this.meterID != null && indexPageMeter.getMeterID() != null &&
				this.meterID.intValue() != indexPageMeter.getMeterID().intValue()){
			return false;
		}
		if((this.meterID == null && indexPageMeter.getMeterID() != null) ||
				(this.meterID != null && indexPageMeter.getMeterID() == null)){
			return false;
		}
		return true;
	}
}
