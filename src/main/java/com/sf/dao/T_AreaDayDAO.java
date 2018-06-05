package com.sf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.T_AreaDay;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface T_AreaDayDAO {
	
	/**
	 * 根据条件查询T_AreaDay数据，条件里面参数可以为空，例如，selectDay = null,
	 * 则代表查询的是到月的数据
	 * @param areaID
	 * @param selectYear
	 * @param selectMonth
	 * @param selectDay
	 * @return
	 */
	public List<T_AreaDay> getT_AreaDay(@Param("areaID") Integer areaID, @Param("selectYear") Integer selectYear,
                                        @Param("selectMonth") Integer selectMonth, @Param("selectDay") Integer selectDay);
	
	/**
	 * 根据条件查询 进出水总和，条件里面参数可以为空，例如，selectDay = null,
	 * 则代表查询的是到月的数据
	 * @param areaID
	 * @param selectYear
	 * @param selectMonth
	 * @param selectDay
	 * @return
	 */
	public Map<String,Object> getSumGrossInAndGrossOut(@Param("areaID") Integer areaID, @Param("selectYear") Integer selectYear,
                                                       @Param("selectMonth") Integer selectMonth, @Param("selectDay") Integer selectDay);
	
	/**
	 * 
	 * @param meterIDList
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	List<T_AreaDay> getT_AreaDayBetween(@Param("areaIDList") List<Integer> areaIDList,
                                        @Param("startDateStr") String startDateStr,
                                        @Param("endDateStr") String endDateStr);
}
