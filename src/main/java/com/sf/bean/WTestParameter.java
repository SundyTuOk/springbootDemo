package com.sf.bean;

import java.math.BigDecimal;
import java.util.Date;

public class WTestParameter {
  
/*	 <id column="WTestParameter_ID" property="wTestParameter_ID"/>
     <result column="Area_ID" property="area_ID"/>
     <result column="IsAuto" property="isAuto"/>
     <result column="Enable" property="enable"/>
     <result column="MonitoringTime" property="monitoringTime"/>
     <result column="BJ_leakage" property="bJ_leakage"/>
     <result column="GY_leakage" property="gY_leakage"/>
     <result column="OrderIndex" property="orderIndex"/>*/
	private Integer wTestParameterID;
	private Integer areaID;
	private Integer isAuto;
	private Integer enable;
	private String monitoringTime;
	private BigDecimal bJ_leakage;
	private BigDecimal gY_leakage;
	private Integer orderIndex;
	public Integer getwTestParameterID() {
		return wTestParameterID;
	}
	public void setwTestParameterID(Integer wTestParameterID) {
		this.wTestParameterID = wTestParameterID;
	}
	public Integer getAreaID() {
		return areaID;
	}
	public void setAreaID(Integer areaID) {
		this.areaID = areaID;
	}
	public Integer getIsAuto() {
		return isAuto;
	}
	public void setIsAuto(Integer isAuto) {
		this.isAuto = isAuto;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public String getMonitoringTime() {
		return monitoringTime;
	}
	public void setMonitoringTime(String monitoringTime) {
		this.monitoringTime = monitoringTime;
	}
	public BigDecimal getbJ_leakage() {
		return bJ_leakage;
	}
	public void setbJ_leakage(BigDecimal bJ_leakage) {
		this.bJ_leakage = bJ_leakage;
	}
	public BigDecimal getgY_leakage() {
		return gY_leakage;
	}
	public void setgY_leakage(BigDecimal gY_leakage) {
		this.gY_leakage = gY_leakage;
	}
	public Integer getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	
} 
