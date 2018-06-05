package com.sf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.IndexPageMeter;
import com.sf.bean.Meter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IndexPageMeterDAO {
	/**
	 * 添加一条IndexPageMeter记录
	 * @param indexPageMeter
	 * @return
	 */
	public int addIndexPageMeter(IndexPageMeter indexPageMeter);
	
	/**
	 * 查询IndexPageMeter,传入的参数在IndexPageMeter，就是要找到这条记录,看下有没有
	 * @return
	 */
	public IndexPageMeter getIndexPageMeter(IndexPageMeter indexPageMeter);
	
	/**
	 * 根据IndexPageMeter表里面的MeterID查找所有的设备
	 * @param indexPageMeterID
	 * @return
	 */
	public List<Meter> getMeterFromIndexPageMeterID(@Param("areaID") int areaID, @Param("userID") String userID);
}
