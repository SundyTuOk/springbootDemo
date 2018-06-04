package com.sf.bean;

import java.util.Date;

public class ErrList {
	private Integer errID;
	private Integer errType;
	private Integer areaID;
	private String errTitle;
	private Date errTime;
	private Date insertTime;
	public Integer getErrID() {
		return errID;
	}
	public void setErrID(Integer errID) {
		this.errID = errID;
	}
	public Integer getErrType() {
		return errType;
	}
	public void setErrType(Integer errType) {
		this.errType = errType;
	}
	public Integer getAreaID() {
		return areaID;
	}
	public void setAreaID(Integer areaID) {
		this.areaID = areaID;
	}
	public String getErrTitle() {
		return errTitle;
	}
	public void setErrTitle(String errTitle) {
		this.errTitle = errTitle;
	}
	public Date getErrTime() {
		return errTime;
	}
	public void setErrTime(Date errTime) {
		this.errTime = errTime;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
}
