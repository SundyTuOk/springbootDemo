package com.sf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.DictionaryValue;
import org.springframework.context.annotation.Lazy;

@Mapper
@Lazy(value = true)
public interface DictionaryValueDAO {

	/**
	 * 
	 * @param start
	 * @return
	 */
	public List<DictionaryValue> getDictionaryValueByDictionaryID(@Param("dictionaryID") int dictionaryID);
	
	public List<Map<String,Object>> getTypeAndValueByDictionaryValueNum(@Param("dictionaryValueNum") String dictionaryValueNum);
	
	public List<Map<String,Object>> getTypeAndValueByDictionaryValueNumAndMeterID(@Param("dictionaryValueNum") String dictionaryValueNum, @Param("meterID") Integer meterID);
	/**
	 * 根据@Param("dictionaryValueNum") String dictionaryValueNum查询DictionaryValue对象
	 * @param dictionaryValueNum
	 * @return
	 */
	public DictionaryValue getDictionaryValueByDictionaryValueNum(@Param("dictionaryValueNum") String dictionaryValueNum);
}
