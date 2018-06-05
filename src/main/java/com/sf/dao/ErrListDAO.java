package com.sf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.ErrList;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ErrListDAO {
	
	/**
	 * 根据AreaID和errType查询ErrList对象，条件里面可传入null
	 * @param areaID
	 * @param errType
	 * @return
	 */
	List<Map<String,Object>> getErrListByAreaIDAndErrType(@Param("areaID") Integer areaID, @Param("errType") String errType);
}
