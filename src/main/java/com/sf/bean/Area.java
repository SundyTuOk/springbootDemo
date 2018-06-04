package com.sf.bean;

public class Area {
	
	private Integer areaID;
	private String areaName;
	private String areaNum;
	private String DLModeFileAddress;
	private String JGModeFileAddress;
	private Integer SuperiorAreaID;
	private String AreaList;
	
	
	public Integer getAreaID() {
		return areaID;
	}
	public void setAreaID(Integer areaID) {
		this.areaID = areaID;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getDLModeFileAddress() {
		return DLModeFileAddress;
	}
	public void setDLModeFileAddress(String dLModeFileAddress) {
		DLModeFileAddress = dLModeFileAddress;
	}
	public String getJGModeFileAddress() {
		return JGModeFileAddress;
	}
	public void setJGModeFileAddress(String jGModeFileAddress) {
		JGModeFileAddress = jGModeFileAddress;
	}
	public Integer getSuperiorAreaID() {
		return SuperiorAreaID;
	}
	public void setSuperiorAreaID(Integer superiorAreaID) {
		SuperiorAreaID = superiorAreaID;
	}
	public String getAreaList() {
		return AreaList;
	}
	public void setAreaList(String areaList) {
		AreaList = areaList;
	}
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	
}
