package com.sf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.Meter;
import com.sf.bean.ZWA;
import org.springframework.context.annotation.Lazy;

@Mapper
@Lazy(value = true)
public interface MeterDAO {
	
	/**
	 * 查询meter表里面总共有多少行
	 * @return
	 */
	public int getAllCount();
	
	/**
	 * 查询数据库所有meter对象
	 * @return
	 */
	public List<Meter> getAllMeters();
	
	/**
	 * 分页查询meter对象,查询从start到end的数据
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Meter> getAllMetersByLimitPageAndSort(
            @Param("start") int start,
            @Param("end") int end,
            @Param("sortName") String sortName,
            @Param("sortType") String sortType);

	/**
	 * 插入一条Meter
	 * @param meter
	 * @return
	 */
	public int addMeter(Meter meter);

	/**
	 * 批量删除Meter
	 * @param meterNums
	 * @return
	 */
	public int deleteMeterByMeterNums(String[] meterNums);

	/**
	 * 更新一条Meter
	 * @param meter
	 * @return
	 */
	public int updateMeter(Meter meter);
	
	/**
	 * 根据meterNum查找Meter
	 * @param meterNum
	 * @return
	 */
	public Meter getMeterByMeterNum(String meterNum);
	
	/**
	 * 获取Meter表所有列
	 * @return
	 */
	public List<String> getColums();
	
	/**
	 * 根据所传入where条件查询所有符合的Meter
	 * @param where
	 * @return
	 */
	public List<Meter> getMetersByWhereLimit(
            @Param("start") int start,
            @Param("end") int end,
            @Param("where") String where,
            @Param("sortName") String sortName,
            @Param("sortType") String sortType);

	/**
	 * 根据where条件查询一共有多少行
	 * @param string
	 * @return
	 */
	public int getAllCountByWhere(@Param("where") String where);
	
	/**
	 * 根据meterNums获取meterID
	 * @param meterNums
	 * @return
	 */
	public List<Integer> getMeterIDByMeterNums(String[] meterNums);
	
	/**
	 * 查询前limitSize条Meter数据，并且根据后面的meterNum和meterName模糊查询
	 * 根据meterType查询Meter，并且有模糊查询和限制条数
	 * @param limitSize
	 * @param meterNum
	 * @param meterName
	 * @return
	 */
	public List<Meter> getMeterByMeterTypeWithLimitSizeAndFuzzy(
            @Param("limitSize") int limitSize,
            @Param("meterType") String meterType,
            @Param("meterNum") String meterNum,
            @Param("meterName") String meterName);
	
	/**
	 * 查询前limitSize条Meter数据，并且根据后面的meterNum和meterName模糊查询
	 * 并且有模糊查询和限制条数
	 * @param limitSize
	 * @param meterNum
	 * @param meterName
	 * @return
	 */
	public List<Meter> getMeterWithLimitSizeAndFuzzy(
            @Param("limitSize") int limitSize,
            @Param("meterNum") String meterNum,
            @Param("meterName") String meterName);
	
	/**
	 * 根据meterID查找Meter
	 * @param meterID
	 * @return
	 */
	public Meter getMeterByMeterID(int meterID);
	
	/**
	 * 根据meterIDs查找Meters
	 * @param meterIDs
	 * @return
	 */
	public List<Meter> getMeterByMeterIDs(List<Integer> meterIDs);
	
	/**
	 * 根据meterType查找Meter
	 * @param meterID
	 * @return
	 */
	public List<Meter> getMeterByMeterType(String meterType);
	/**
	 * 获取表计时间段内所有数据点
	 * @param meterID
	 * @param StartTime 起始时间
	 * @param EndTime 终止起始时间
	 * @return
	 */
	public List<ZWA> getWAMeterData(@Param("Meter_ID") String Meter_ID,
                                    @Param("StartTime") String StartTime,
                                    @Param("EndTime") String EndTime,
                                    @Param("MonthArry") String[] MonthArry);
	/**
	 * 获取表计时间段内所有数据点
	 * @param meterID
	 * @param StartTime 起始时间
	 * @param EndTime 终止起始时间
	 * @return
	 */
	public List<Map<String,Object>> getWAMeterDataH(@Param("Meter_ID") String Meter_ID,
                                                    @Param("StartTime") String StartTime,
                                                    @Param("EndTime") String EndTime,
                                                    @Param("MonthArry") String[] MonthArry);
	
	public List<Map<String,Object>> getT_DayAm(@Param("Meter_ID") String Meter_ID,
                                               @Param("StartTime") String StartTime);
	
	public List<Map<String,Object>> getVALUENetWork(String Meter_ID);
	
	/**
	 * 批量删除Meter
	 * @param meterIDs
	 * @return
	 */
	int deleteMeterByMeterIDs(String[] meterIDs);
	
}
