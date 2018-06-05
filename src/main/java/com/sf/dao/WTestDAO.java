package com.sf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.type.Alias;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.sf.bean.Area;
@Mapper
@Repository
public interface WTestDAO {
	

	/**
	 * 查询区块的默认水平衡测试模型ID
	 * @param
	 * @return
	 */
	public String getAreasDefaultWTestParameter_ID(int Area_ID);

	/**
	 * 查询区块的默认水平衡测试模型ID 下的配表信息
	 * @param
	 * @return
	 */
	public List<Map<String,Object>>  getWTestMeter(String WTestParameter_ID);
	/**
	 * 区域日用水量
	 * @param
	 * @return
	 */
	public List<Map<String,Object>>  getAreaDayGross(
            @Param("Area_ID") String Area_ID,
            @Param("StartTime") String StartTime,
            @Param("EndTime") String EndTime);
	
	/**
	 * 区域月用水量
	 * @param
	 * @return
	 */
	public List<Map<String,Object>>  getAreaMonthGross(
            @Param("Area_ID") String Area_ID,
            @Param("StartTime") String StartTime,
            @Param("EndTime") String EndTime);
	
	/**
	 * 获取水平衡测试的列表
	 * @return
	 */
	List<Map<String,Object>> getWaterBalanceAnalysis(
            @Param("start") int start,
            @Param("end") int end,
            @Param("sortName") String sortName,
            @Param("sortType") String sortType);
	
	/**
	 * 根据wTestParameterID删除WTestParameter
	 * @param wTestParameterID
	 * @return
	 */
	int deleteWTestParameterByWTestParameterID(@Param("wTestParameterID") int wTestParameterID);
	
	/**
	 * 模糊查询水平衡分析
	 * @param condition
	 * @return
	 */
	List<Map<String,Object>> getWaterBalanceAnalysisFuzzy(@Param("start") int start,
                                                          @Param("end") int end,
                                                          @Param("sortName") String sortName,
                                                          @Param("sortType") String sortType,
                                                          @Param("condition") String condition);

	/**
	 * 查询水平衡分析总数量
	 * @return
	 */
	int getWaterBalanceAnalysisCount();

	/**
	 * 模糊查询水平衡分析总数量
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	int getWaterBalanceAnalysisFuzzyCount(@Param("condition") String condition);
}
