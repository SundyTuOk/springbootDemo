package com.sf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.Meter;
import com.sf.bean.NetWork;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NetWorkDAO {

	/**
	 * 查询NetWork表里面总共有多少行
	 * 
	 * @return
	 */
	public int getAllCount();

	/**
	 * 分页查询NetWork对象,查询从start到end的数据
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> getAllNetWorkByLimitPage(
            @Param("start") int start,
            @Param("end") int end,
            @Param("sortName") String sortName,
            @Param("sortType") String sortType);
	

	/**
	 * 批量删除NetWork
	 * 
	 * @param meterNums
	 * @return
	 */
	public void deleteNetWorksByNetWorkIDs(String[] netWorkIDs);

	/**
	 * 查询数据 NetWork_Num, NetWork_Name, PipeNetworkType, NetWork_Caliber,
	 * NetWork_TotalLenght, Area_ID
	 * 
	 * @param nw
	 * @return
	 */
	public List<Map<String, Object>> selectNetWorkss(
            @Param("NetWork_Num") String NetWork_Num,
            @Param("NetWork_Name") String NetWork_Name,
            @Param("PipeNetworkType") String PipeNetworkType,
            @Param("NetWork_Caliber") String NetWork_Caliber,
            @Param("NetWork_TotalLenght") String NetWork_TotalLenght,
            @Param("Area_ID") String Area_ID,
            @Param("start") int start,
            @Param("end") int end,
            @Param("sortName") String sortName,
            @Param("sortType") String sortType);

	/**
	 * 查询行数
	 * 
	 * @param NetWork_Num
	 * @param NetWork_Name
	 * @param PipeNetworkType
	 * @param NetWork_Caliber
	 * @param NetWork_TotalLenght
	 * @param Area_ID
	 * @return
	 */
	public int getAllCounts(@Param("NetWork_Num") String NetWork_Num,
                            @Param("NetWork_Name") String NetWork_Name,
                            @Param("PipeNetworkType") String PipeNetworkType,
                            @Param("NetWork_Caliber") String NetWork_Caliber,
                            @Param("NetWork_TotalLenght") String NetWork_TotalLenght,
                            @Param("Area_ID") String Area_ID);

	/**
	 * 根据限制条数 查询前面几条NetWork
	 * 
	 * @param currentSelectPage
	 * @return
	 */
	public List<Map<String, Object>> getNetWorkByLimiteSize(Integer currentSelectSize);

	/**
	 * 根据限制条数 已及模糊查询条件  查询前面几条NetWork
	 * 
	 * @param currentSelectSize
	 * @param searchCode
	 * @param searchName
	 * @return
	 */
	public List<Map<String, Object>> getNetWorkByLimiteSizeAndFuzzy(
            @Param("CurrentSelectSize") int CurrentSelectSize,
            @Param("SearchCode") String SearchCode,
            @Param("SearchName") String SearchName);
	
	/**
	 * 添加一条管网数据记录
	 * @param netWork
	 * @return
	 */
	public int addNetWork(NetWork netWork);
	
	/**
	 *根据地区ID查询下级地区的个数
	 * @param area_ID
	 * @return
	 */
	int getSubordinateRegion(@Param("Area_ID") Integer Area_ID);
	
	/**
	 * 根据地区ID查询下级地区管道的个数
	 * @param area_ID
	 * @return
	 */
	int getAreaNetWork(@Param("Area_ID") Integer Area_ID);
	
	/**
	 * 根据地区ID查询 某种设备的个数
	 * @param areaID
	 * @param meterType 如果为null，则查询所有设备的个数
	 * @return
	 */
	public int getMeterCountByAreaIDAndMeterType(
            @Param("areaID") Integer areaID,
            @Param("meterType") String meterType);
	
	/**
	 * 根据地区ID查询 某种设备
	 * @param areaID
	 * @param meterType 如果为null，则查询所有设备
	 * @return
	 */
	public List<Meter> getMeterByAreaIDAndMeterType(
            @Param("areaID") Integer areaID,
            @Param("meterType") String meterType);
	
	/**
	 * 根据所传netWorkNum找到对应的NetWork
	 * @param netWorkNum
	 * @return
	 */
	public NetWork getNetWorkByNetWorkNum(String netWorkNum);

	/**
	 * 更新NetWork
	 * @param netWork
	 */
	public void updateNetWork(NetWork netWork);
	
	/**
	 * 查找NetWork
	 * @param netWork
	 */
	public List<NetWork> getNetWorkByNetWorkIDs(List<Integer> netWorkIDs);
	
	/**
	 * 查找NetWork
	 * @param areaID
	 */
	public List<NetWork> getNetWorkByAreaID(int areaID);
	
	
	/**
	 * 查找NetWork的父节点ID
	 * @param areaID
	 */
	public List<Map<String,Object>> getNetWorkPNetWork(String NetWork_ID);
	
	/**
	 * 查找父节点信息
	 * @param areaID
	 */
	public List<Map<String,Object>> getAllPNetWork();
	/**
	 * 查找NetWork列表
	 * @param areaID
	 */
	public List<Map<String,Object>> getAllNetWorkList();
	
}
