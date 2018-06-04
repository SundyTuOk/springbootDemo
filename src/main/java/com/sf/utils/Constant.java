package com.sf.utils;

public class Constant {

	public static final String JSON_ROWS = "rows";
	public static final String JSON_COUNT = "totalCount";
	
	//增删改查操作返回json key和value
	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";
	
	public static final String RESULT = "result";
	public static final String EFFECT_COUNT = "effectCount";
	
	// 水表类型
	public static enum METER_TYPE{
		WA,// 水表
		PR,// 压力机
		FL,// 流量计
		VAlVE//阀门
	}
	
	// 配表信息,对应于DictionaryValue里面的DictionaryID
	public static enum ConfigMeterInfo{
		/**
		 * 表计类型
		 */
		meterType(1),// 表计类型
		/**
		 * 设备类型
		 */
		deviceType(3),// 设备类型
		/**
		 * 管网类型
		 */
		netWrokType(4);// 管网类型
		
		private int dictionaryID;

		public int getDictionaryID() {
			return dictionaryID;
		}
		
		private ConfigMeterInfo(int dictionaryID){
			this.dictionaryID = dictionaryID;
		}
	}
}
