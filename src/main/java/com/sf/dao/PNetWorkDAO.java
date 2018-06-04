package com.sf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sf.bean.PNetWork;
import org.springframework.context.annotation.Lazy;

@Mapper
@Lazy(value = true)
public interface PNetWorkDAO {
	
	/**
	 * 增加一条PNetWork
	 * @param pNetWork
	 * @return
	 */
	public int addPNetWork(PNetWork pNetWork);
	
	/**
	 * 根据pNetWorkID和netWorkID删除PNetWork
	 * @param pNetWorkID
	 * @param netWorkID
	 * @return
	 */
	public int deletePNetWorkByNetWorkIDAndPNetWorkID(
            @Param("pNetWorkID") int pNetWorkID,
            @Param("netWorkID") int netWorkID);
	
	/**
	 * 根据netWorkID获取所有的PNetWork
	 * @param netWorkID
	 * @return
	 */
	public List<Map<String,Object>> getPNetWorkByNetWorkID(int netWorkID);
	
	/**
	 * 删除所有NetWorkID、PNetWorkID 为id的数据
	 * @param id
	 * @return
	 */
	public int deletePNetWorkByID(String id);

	/**
	 * 更新PNetWork
	 * @param pNetWork
	 */
	public int updatePNetWork(PNetWork pNetWork);
	
	/**
	 * 更新PNetWork
	 * @param pNetWork
	 */
	public List<PNetWork> getPNetWorkByPNetWorkID(int pNetWorkID);
	
	/**
	 * 删除所有NetWorkID、PNetWorkID 为id的数据
	 * @param id
	 * @return
	 */
	public int deletePNetWorkByIDs(List<Integer> IDs);
	
	/**
	 * 把NetWorkID为所传参数的PNetWork全部删除
	 * @param netWorkID
	 * @return
	 */
	int deletePNetWorkByNetWorID(int netWorkID);
}
