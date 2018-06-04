package com.sf.dao;

import com.sf.bean.Area;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper
@Lazy(value = true)
public interface AreaDAO {
	
	/**
	 * 区块信息获取
	 * @return
	 */
	public List<Area> getAllAreas();
	
	/**
	 * 新增一条区域记录
	 * @param area
	 * @return
	 */
	public int addArea(Area area);
	
	/**
	 * 批量删除区域记录
	 * @param area
	 * @return
	 */
	public int deleteArea(String areaNum);
	
	/**
	 * 批量删除区域记录
	 * @param area
	 * @return
	 */
	public int updateArea(Area area);
	
	/**
	 * 通过areaID查询到Area
	 * @param areaID
	 * @return
	 */
	public Area getAreaByAreaID(int areaID);
	
	/**
	 * 得到areaID最大的一条记录
	 * @param areaID
	 * @return
	 */
	public Area getLastArea();
	
	/**
	 * 根据areaNum找到Area
	 * @param areaNum
	 * @return
	 */
	public Area getAreaByAreaNum(String areaNum);
	
	/**
	 * 查询所有父id为superAreaID的区块信息
	 * @param superAreaID
	 * @return
	 */
	public List<Area> getAreasBySuperiorAreaID(int superAreaID);

	

}
