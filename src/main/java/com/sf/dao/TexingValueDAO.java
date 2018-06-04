package com.sf.dao;

import java.util.List;
import java.util.Map;

import com.sf.bean.TexingValue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Lazy;

@Mapper
@Lazy(value = true)
public interface TexingValueDAO {
	
	/**
	 * 添加一条TexingValue
	 * @param texingValue
	 */
	public void addTexingValue(TexingValue texingValue);
	
	/**
	 * 更新一条TexingValue
	 * @param texingValue
	 */
	public void updateTexingValue(TexingValue texingValue);
	
	/**
	 * 根据meterID删除Texingvalue数据
	 * @param meterID
	 * @return
	 */
	public int deleteTexingValueByMeterID(int meterID);
	
	/**
	 * 根据meterID查询这个设备的所有特性
	 * @param meterID
	 * @return
	 */
	public List<Map<String,Object>> getMeterTexingByMeterID(int meterID);
}
