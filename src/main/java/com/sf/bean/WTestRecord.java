package com.sf.bean;

import java.math.BigDecimal;
import java.util.Date;

public class WTestRecord {
  
	private Integer wTestRecordid;
	private Integer wTestParameterid;
	private Integer wModeid;
	private String rsMeterIDList;
	private String csMeterIDList;
	private Date testTime;
	private BigDecimal totalWaterQuantity;
	private BigDecimal userWaterQuantity;
	private BigDecimal totalLeakage;
	private BigDecimal bjLeakage;
	private BigDecimal tubeLeakage;
	private BigDecimal gyValue;
	
	
	public Integer getwTestRecordid() {
		return wTestRecordid;
	}
	public void setwTestRecordid(Integer wTestRecordid) {
		this.wTestRecordid = wTestRecordid;
	}
	public Integer getwTestParameterid() {
		return wTestParameterid;
	}
	public void setwTestParameterid(Integer wTestParameterid) {
		this.wTestParameterid = wTestParameterid;
	}
	public Integer getwModeid() {
		return wModeid;
	}
	public void setwModeid(Integer wModeid) {
		this.wModeid = wModeid;
	}
	public String getRsMeterIDList() {
		return rsMeterIDList;
	}
	public void setRsMeterIDList(String rsMeterIDList) {
		this.rsMeterIDList = rsMeterIDList;
	}
	public String getCsMeterIDList() {
		return csMeterIDList;
	}
	public void setCsMeterIDList(String csMeterIDList) {
		this.csMeterIDList = csMeterIDList;
	}
	public Date getTestTime() {
		return testTime;
	}
	public void setTestTime(Date testTime) {
		this.testTime = testTime;
	}
	public BigDecimal getTotalWaterQuantity() {
		return totalWaterQuantity;
	}
	public void setTotalWaterQuantity(BigDecimal totalWaterQuantity) {
		this.totalWaterQuantity = totalWaterQuantity;
	}
	public BigDecimal getUserWaterQuantity() {
		return userWaterQuantity;
	}
	public void setUserWaterQuantity(BigDecimal userWaterQuantity) {
		this.userWaterQuantity = userWaterQuantity;
	}
	public BigDecimal getTotalLeakage() {
		return totalLeakage;
	}
	public void setTotalLeakage(BigDecimal totalLeakage) {
		this.totalLeakage = totalLeakage;
	}
	public BigDecimal getBjLeakage() {
		return bjLeakage;
	}
	public void setBjLeakage(BigDecimal bjLeakage) {
		this.bjLeakage = bjLeakage;
	}
	public BigDecimal getTubeLeakage() {
		return tubeLeakage;
	}
	public void setTubeLeakage(BigDecimal tubeLeakage) {
		this.tubeLeakage = tubeLeakage;
	}
	public BigDecimal getGyValue() {
		return gyValue;
	}
	public void setGyValue(BigDecimal gyValue) {
		this.gyValue = gyValue;
	}
	
}
