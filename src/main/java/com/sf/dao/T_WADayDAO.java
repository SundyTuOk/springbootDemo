package com.sf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.T_WADay;
import org.springframework.context.annotation.Lazy;

@Mapper
@Lazy(value = true)
public interface T_WADayDAO {
	
	/**
	 * 根据meterID查询数据，如果输入时间准确到年月日，则只会查询回来一条数据
	 * 反之，如果年月日参数 有为null，则返回多条数据
	 * @param meterID
	 * @param selectYear
	 * @param selectMonth
	 * @param selectDay
	 * @return
	 */
	public List<T_WADay> getT_WADayBygetT_WADayByMeterIDAndDate(
            @Param("meterID") int meterID,
            @Param("selectYear") Integer selectYear,
            @Param("selectMonth") Integer selectMonth,
            @Param("selectDay") Integer selectDay);
	
	/**
	 * 获取两个日期之间的数据
	 * @param startDateStr 例如："2018-4-01" 或者"2018-04-01"
	 * @param endDateStr
	 * @return
	 */
	List<T_WADay> getT_WADayBetween(@Param("meterIDList") List<Integer> meterIDList,
                                    @Param("startDateStr") String startDateStr,
                                    @Param("endDateStr") String endDateStr);
}
