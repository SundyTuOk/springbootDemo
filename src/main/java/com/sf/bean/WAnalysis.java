package com.sf.bean;

import java.math.BigDecimal;
import java.util.Date;

public class WAnalysis {
   
	
	private Integer     wAnalysisId;
	private Date        evaluationTime;
	private Integer     wModeID; 
	private BigDecimal  inputWaterVolume;
	private BigDecimal  tollWaterVolume;
	private BigDecimal  noTollWaterVolume;
	private BigDecimal  FREEWaterVolume;
	private BigDecimal  noFREEWaterVolume;
	private BigDecimal  noAuthorizedWaterVolume;
	private BigDecimal  ybErrorWaterVolume;
	private BigDecimal  sPLost;
	private BigDecimal  xcCOverflow;
	private BigDecimal  yhljLost;
	private BigDecimal  gwPressure;
	private BigDecimal  zggLength;
	private BigDecimal  psgdLength;
	private BigDecimal  yhljgLength;
	private Integer     numberOfContacts;
	private Integer     serviceLevel;
	private BigDecimal  leakageRate;
	private BigDecimal  cxDifference;
	private BigDecimal  dwgcLost;
	private BigDecimal  jdLeakage;
	
	
	public Integer getwAnalysisId() {
		return wAnalysisId;
	}
	public void setwAnalysisId(Integer wAnalysisId) {
		this.wAnalysisId = wAnalysisId;
	}
	public Date getEvaluationTime() {
		return evaluationTime;
	}
	public void setEvaluationTime(Date evaluationTime) {
		this.evaluationTime = evaluationTime;
	}
	public Integer getwModeID() {
		return wModeID;
	}
	public void setwModeID(Integer wModeID) {
		this.wModeID = wModeID;
	}
	public BigDecimal getInputWaterVolume() {
		return inputWaterVolume;
	}
	public void setInputWaterVolume(BigDecimal inputWaterVolume) {
		this.inputWaterVolume = inputWaterVolume;
	}
	public BigDecimal getTollWaterVolume() {
		return tollWaterVolume;
	}
	public void setTollWaterVolume(BigDecimal tollWaterVolume) {
		this.tollWaterVolume = tollWaterVolume;
	}
	public BigDecimal getNoTollWaterVolume() {
		return noTollWaterVolume;
	}
	public void setNoTollWaterVolume(BigDecimal noTollWaterVolume) {
		this.noTollWaterVolume = noTollWaterVolume;
	}
	public BigDecimal getFREEWaterVolume() {
		return FREEWaterVolume;
	}
	public void setFREEWaterVolume(BigDecimal fREEWaterVolume) {
		FREEWaterVolume = fREEWaterVolume;
	}
	public BigDecimal getNoFREEWaterVolume() {
		return noFREEWaterVolume;
	}
	public void setNoFREEWaterVolume(BigDecimal noFREEWaterVolume) {
		this.noFREEWaterVolume = noFREEWaterVolume;
	}
	public BigDecimal getNoAuthorizedWaterVolume() {
		return noAuthorizedWaterVolume;
	}
	public void setNoAuthorizedWaterVolume(BigDecimal noAuthorizedWaterVolume) {
		this.noAuthorizedWaterVolume = noAuthorizedWaterVolume;
	}
	public BigDecimal getYbErrorWaterVolume() {
		return ybErrorWaterVolume;
	}
	public void setYbErrorWaterVolume(BigDecimal ybErrorWaterVolume) {
		this.ybErrorWaterVolume = ybErrorWaterVolume;
	}
	public BigDecimal getsPLost() {
		return sPLost;
	}
	public void setsPLost(BigDecimal sPLost) {
		this.sPLost = sPLost;
	}
	public BigDecimal getXcCOverflow() {
		return xcCOverflow;
	}
	public void setXcCOverflow(BigDecimal xcCOverflow) {
		this.xcCOverflow = xcCOverflow;
	}
	public BigDecimal getYhljLost() {
		return yhljLost;
	}
	public void setYhljLost(BigDecimal yhljLost) {
		this.yhljLost = yhljLost;
	}
	public BigDecimal getGwPressure() {
		return gwPressure;
	}
	public void setGwPressure(BigDecimal gwPressure) {
		this.gwPressure = gwPressure;
	}
	public BigDecimal getZggLength() {
		return zggLength;
	}
	public void setZggLength(BigDecimal zggLength) {
		this.zggLength = zggLength;
	}
	public BigDecimal getPsgdLength() {
		return psgdLength;
	}
	public void setPsgdLength(BigDecimal psgdLength) {
		this.psgdLength = psgdLength;
	}
	public BigDecimal getYhljgLength() {
		return yhljgLength;
	}
	public void setYhljgLength(BigDecimal yhljgLength) {
		this.yhljgLength = yhljgLength;
	}
	public Integer getNumberOfContacts() {
		return numberOfContacts;
	}
	public void setNumberOfContacts(Integer numberOfContacts) {
		this.numberOfContacts = numberOfContacts;
	}
	public Integer getServiceLevel() {
		return serviceLevel;
	}
	public void setServiceLevel(Integer serviceLevel) {
		this.serviceLevel = serviceLevel;
	}
	public BigDecimal getLeakageRate() {
		return leakageRate;
	}
	public void setLeakageRate(BigDecimal leakageRate) {
		this.leakageRate = leakageRate;
	}
	public BigDecimal getCxDifference() {
		return cxDifference;
	}
	public void setCxDifference(BigDecimal cxDifference) {
		this.cxDifference = cxDifference;
	}
	public BigDecimal getDwgcLost() {
		return dwgcLost;
	}
	public void setDwgcLost(BigDecimal dwgcLost) {
		this.dwgcLost = dwgcLost;
	}
	public BigDecimal getJdLeakage() {
		return jdLeakage;
	}
	public void setJdLeakage(BigDecimal jdLeakage) {
		this.jdLeakage = jdLeakage;
	}
	
	
	
}
