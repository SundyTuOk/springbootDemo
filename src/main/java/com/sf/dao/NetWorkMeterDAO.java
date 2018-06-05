package com.sf.dao;

import java.util.List;
import java.util.Map;

import com.sf.bean.NetWork;
import com.sf.bean.NetWorkMeter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NetWorkMeterDAO {
	
	/**
	 * 添加一条NetWorkMeter
	 * @param netWorkMeter
	 * @return
	 */
	public int addNetWorkMeter(NetWorkMeter netWorkMeter);
	
	/**
	 * 根据netWorkMeterID删除NetWorkMeter
	 * @param netWorkMeterID
	 * @return
	 */
	public int deleteNetWorkMeterByNetWorkMeterIDs(List<Integer> netWorkMeterID);
	
	/**
	 * 根据netWorkID查询所有的NetWorkMeter
	 * @param netWorkID
	 * @return
	 */
	public List<Map<String,Object>> getNetWorkMeterListByNetWorkID(int netWorkID);

	/**
	 * 更新NetWorkMeter
	 * @param netWorkMeter
	 */
	public int updateNetWorkMeter(NetWorkMeter netWorkMeter);
	
	/**
	 * 根据netWorkID查询所有的NetWorkMeter
	 * @param netWorkID
	 * @return
	 */
	public List<NetWorkMeter> getNetWorkMeterByNetWorkID(int netWorkID);
	
	/**
	 * 根据netWorkID删除所有的NetWorkMeter
	 * @param netWorkID
	 * @return
	 */
	int deleteNetWorkMeterByNetWorkID(int netWorkID);
}
