package com.sf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ZDAO {
	
	/**
	 * 根据meterID查询某段时间,Z_FL所有的数据
	 * @param meterID 如果为null，则查询这段时间所有meter的数据
	 * @param start 起始时间
	 * @param end	终止时间	
	 * @param tables	需要用工具类方法CommomUtils.getTableNameSuffix(Date start,Date end)
	 * @return
	 */
	public List<Map<String,Object>> getPRByMeterIDBetweenStartAndEnd(@Param("meterID") int meterID,
                                                                     @Param("start") Date start, @Param("end") Date end, @Param("tableSuffixArr") String[] tableSuffixArr);


	/**
	 * 获取上传设备的数据
	 * @param UpFrom 第三方系统设备来源
	 * @param UpID 设备第三方系统中的ID
	 * @return
	 */
	public List<Map<String,Object>> getMeterInfoForSend(@Param("UpFrom") String UpFrom,
                                                        @Param("UpID") String UpID, @Param("Meter_Type") String Meter_Type);
	
	
	/**
	 * 更新数据库中设备档案
	 * @param UpFrom 第三方系统设备来源
	 * @param UpID 设备第三方系统中的ID
	 * @return
	 */
	public void EditMeterInfoForSend(@Param("Meter_ID") String Meter_ID,
                                     @Param("Meter_Num") String Meter_Num,
                                     @Param("Meter_Name") String Meter_Name,
                                     @Param("Meter_Changjia") String Meter_Changjia,
                                     @Param("Meter_485Address") String Meter_485Address,
                                     @Param("Beilv") String Beilv,
                                     @Param("Xiuzheng") String Xiuzheng);

	/**
	 * 添加数据库中设备档案
	 * @param UpFrom 第三方系统设备来源
	 * @param UpID 设备第三方系统中的ID
	 * @return
	 */
	public void AddMeterInfoForSend(
            @Param("Meter_Num") String Meter_Num,
            @Param("Meter_Name") String Meter_Name,
            @Param("Meter_Changjia") String Meter_Changjia,
            @Param("Meter_485Address") String Meter_485Address,
            @Param("Beilv") String Beilv,
            @Param("Xiuzheng") String Xiuzheng,
            @Param("Meter_Type") String Meter_Type,
            @Param("UpFrom") String UpFrom,
            @Param("UpID") String UpID);
	
	/**
	 * 添加抄读数据
	 * @param UpFrom 第三方系统设备来源
	 * @param UpID 设备第三方系统中的ID
	 * @return
	 */
	public void AddMeterDatasForSend(
            @Param("Meter_Type") String Meter_Type,
            @Param("Meter_ID") String Meter_ID,
            @Param("ValueTime") String ValueTime,
            @Param("ZvalueZY") String ZvalueZY,
            @Param("UpFrom") String UpFrom);
	
	/**
	 * 更新数据库中设备档案
	 * @param UpFrom 第三方系统设备来源
	 * @param UpID 设备第三方系统中的ID
	 * @return
	 */
	public void EditMeterValueForSend(@Param("Meter_ID") String Meter_ID,
                                      @Param("Meter_Time") String Meter_Time,
                                      @Param("Meter_Value1") String Meter_Value1);
	
	/**
	 * 新建数据表
	 * @param UpFrom 第三方系统设备来源
	 * @param UpID 设备第三方系统中的ID
	 * @return
	 */
	public void CreateDataTablePrc(
            @Param("Meter_Type") String Meter_ID,
            @Param("tableSuffix") String tableSuffix);
	
	

}
