package com.sf.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.sf.dao.WTestDAO;
/**
 * 水平衡测试的service
 * @author 涂召亮
 *
 */
@Service("wTestService")
@Lazy(value = true)
public class WTestService {
//	@Resource(name = "wTestDAO")
	@Autowired
	private WTestDAO wTestDAO;
	
	/**
	 * 获取水平衡测试的列表
	 * @return
	 */
	public List<Map<String,Object>> getWaterBalanceAnalysis(int start,int end,
			String sortName,String sortType){
		return wTestDAO.getWaterBalanceAnalysis(start,end,
				sortName,sortType);
	}
	
	/**
	 * 根据wTestParameterID删除WTestParameter
	 * @param wTestParameterID
	 * @return
	 */
	public boolean deleteWTestParameterByWTestParameterID(int wTestParameterID){
		int count = wTestDAO.deleteWTestParameterByWTestParameterID(wTestParameterID);
		if(count == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 模糊查询水平衡分析
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> getWaterBalanceAnalysisFuzzy(int start,int end,String sortName,
			String sortType,String condition){
		return wTestDAO.getWaterBalanceAnalysisFuzzy(start,end,sortName,sortType,condition);
	}
	
	/**
	 * 模糊查询水平衡分析
	 * @param condition
	 * @return
	 */
	public int getWaterBalanceAnalysisFuzzyCount(String condition){
		return wTestDAO.getWaterBalanceAnalysisFuzzyCount(condition);
	}

	/**
	 * 查询水平衡分析总数量
	 * @return
	 */
	public int getWaterBalanceAnalysisCount() {
		return wTestDAO.getWaterBalanceAnalysisCount();
	}
}
