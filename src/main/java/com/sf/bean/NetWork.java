package com.sf.bean;

import java.math.BigDecimal;
import java.util.Date;

public class NetWork {
	private Integer     networkID;
	private String      networkNum;
	private String      networkName;
	private String      networkCaliber;
	private Float  networkTotalLenght;
	private Integer     areaID;
	private Date        lastMissedTime;
	private Integer     pipeNetworkType;
	public Integer getNetworkID() {
		return networkID;
	}
	public void setNetworkID(Integer networkID) {
		this.networkID = networkID;
	}
	public String getNetworkNum() {
		return networkNum;
	}
	public void setNetworkNum(String networkNum) {
		this.networkNum = networkNum;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public String getNetworkCaliber() {
		return networkCaliber;
	}
	public void setNetworkCaliber(String networkCaliber) {
		this.networkCaliber = networkCaliber;
	}
	
	public Float getNetworkTotalLenght() {
		return networkTotalLenght;
	}
	public void setNetworkTotalLenght(Float networkTotalLenght) {
		this.networkTotalLenght = networkTotalLenght;
	}
	public Integer getAreaID() {
		return areaID;
	}
	public void setAreaID(Integer areaID) {
		this.areaID = areaID;
	}
	public Date getLastMissedTime() {
		return lastMissedTime;
	}
	public void setLastMissedTime(Date lastMissedTime) {
		this.lastMissedTime = lastMissedTime;
	}
	public Integer getPipeNetworkType() {
		return pipeNetworkType;
	}
	public void setPipeNetworkType(Integer pipeNetworkType) {
		this.pipeNetworkType = pipeNetworkType;
	}
}
