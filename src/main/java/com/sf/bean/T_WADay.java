package com.sf.bean;

import java.util.Date;

public class T_WADay {

	private Integer meterID;
	private Integer selectYear;
	private Integer selectMonth;
	private Integer selectDay;
	private Date starReadingDate;
	private Date endReadingDate;
	private Float starZValueZY;
	private Float endZValueZY;
	private Float zGross;
	public Integer getMeterID() {
		return meterID;
	}
	public void setMeterID(Integer meterID) {
		this.meterID = meterID;
	}
	public Integer getSelectYear() {
		return selectYear;
	}
	public void setSelectYear(Integer selectYear) {
		this.selectYear = selectYear;
	}
	public Integer getSelectMonth() {
		return selectMonth;
	}
	public void setSelectMonth(Integer selectMonth) {
		this.selectMonth = selectMonth;
	}
	public Integer getSelectDay() {
		return selectDay;
	}
	public void setSelectDay(Integer selectDay) {
		this.selectDay = selectDay;
	}
	public Date getStarReadingDate() {
		return starReadingDate;
	}
	public void setStarReadingDate(Date starReadingDate) {
		this.starReadingDate = starReadingDate;
	}
	public Date getEndReadingDate() {
		return endReadingDate;
	}
	public void setEndReadingDate(Date endReadingDate) {
		this.endReadingDate = endReadingDate;
	}
	public Float getStarZValueZY() {
		return starZValueZY;
	}
	public void setStarZValueZY(Float starZValueZY) {
		this.starZValueZY = starZValueZY;
	}
	public Float getEndZValueZY() {
		return endZValueZY;
	}
	public void setEndZValueZY(Float endZValueZY) {
		this.endZValueZY = endZValueZY;
	}
	public Float getzGross() {
		return zGross;
	}
	public void setzGross(Float zGross) {
		this.zGross = zGross;
	}
	
}
