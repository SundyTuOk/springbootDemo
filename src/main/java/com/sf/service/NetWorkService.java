package com.sf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import com.sf.bean.Area;
import com.sf.bean.DictionaryValue;
import com.sf.bean.Meter;
import com.sf.bean.NetWork;
import com.sf.bean.NetWorkMeter;
import com.sf.bean.PNetWork;
import com.sf.commonbase.Transaction;
import com.sf.dao.AreaDAO;
import com.sf.dao.DictionaryValueDAO;
import com.sf.dao.MeterDAO;
import com.sf.dao.NetWorkDAO;
import com.sf.dao.NetWorkMeterDAO;
import com.sf.dao.PNetWorkDAO;

@Service("netWorkService")
@Lazy(value = true)
public class NetWorkService {
	
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private Transaction transaction;

	@Autowired
	private DictionaryValueDAO dictionaryValueDAO;

	@Autowired
	private NetWorkMeterDAO netWorkMeterDAO;

	@Autowired
	private PNetWorkDAO pNetWorkDAO;

	@Autowired
	private NetWorkDAO netWorkDAO;

	@Autowired
	private AreaDAO areaDAO;

	@Autowired
	private MeterDAO meterDAO;
	
	public String getNetworkProfile(){
		
		return null;
	}

	public int getAllCount() {
		return netWorkDAO.getAllCount();
	}
	
	public int getAllCounts(String NetWork_Num,String NetWork_Name, String PipeNetworkType,String NetWork_Caliber, String NetWork_TotalLenght, String Area_ID) {
		 return netWorkDAO.getAllCounts(NetWork_Num,NetWork_Name,PipeNetworkType,NetWork_Caliber,NetWork_TotalLenght,Area_ID);
	}

	public List<Map<String, Object>> getAllNetWorkByLimitPage(int start,int end,
			String sortName,String sortType) {
		return netWorkDAO.getAllNetWorkByLimitPage(start, end,sortName,sortType);
		
	}
  
	public List<DictionaryValue> getDictionaryValueByDictionaryID(
			int Dictionary_ID) {
		return dictionaryValueDAO.getDictionaryValueByDictionaryID(Dictionary_ID);
	}
    
	public boolean deleteNetWorks(String[] netWorkIDs) {
		List<Integer> netWorkIDList = new ArrayList<Integer>();
		TransactionStatus status = transaction.beginTansaction();
		try{
			netWorkDAO.deleteNetWorksByNetWorkIDs(netWorkIDs);	
			for(String netWorkID : netWorkIDs){
//				pNetWorkDAO.deletePNetWorkByID(netWorkID);
				int netWorkIDInt = Integer.valueOf(netWorkID);
				netWorkIDList.add(netWorkIDInt);
			}
			pNetWorkDAO.deletePNetWorkByIDs(netWorkIDList);
			netWorkMeterDAO.deleteNetWorkMeterByNetWorkMeterIDs(netWorkIDList);
			transaction.commit(status);
			return true;
		}catch(Exception e){
			logger.error(e);
			logger.error("删除管网失败...");
			transaction.rollback(status);
			return false;
		}
	}
	
	public List<Map<String, Object>> selectNetWorkss(String NetWork_Num,
			String NetWork_Name, String PipeNetworkType,String NetWork_Caliber, 
			String NetWork_TotalLenght, String Area_ID,int start,int end,String sortName,String sortType) {
		 return netWorkDAO.selectNetWorkss(NetWork_Num,NetWork_Name,
				 PipeNetworkType,NetWork_Caliber,NetWork_TotalLenght,
				 Area_ID,start,end,sortName,sortType);
	}
	
	public List<Map<String, Object>> getNetWorkByLimiteSize(Integer currentSelectSize) {
		return netWorkDAO.getNetWorkByLimiteSize(currentSelectSize);
	}

	public List<Map<String, Object>> getNetWorkByLimiteSizeAndFuzzy(int CurrentSelectSize, String SearchCode, String SearchName) {
		return netWorkDAO.getNetWorkByLimiteSizeAndFuzzy(CurrentSelectSize,SearchCode,SearchName);
	}
	
	/**
	 * 添加一条管网，并且把这个管网的父节点，配表信息都设置好
	 * @param netWork
	 * @param pNetWorkList
	 * @return
	 */
	public boolean addNetWorkWithRelate(NetWork netWork,List<PNetWork> pNetWorkList,List<NetWorkMeter> netWorkMeterList){
		TransactionStatus status = transaction.beginTansaction();
		try{
			netWorkDAO.addNetWork(netWork);
			for(PNetWork pNetWork : pNetWorkList){
				pNetWork.setNetWorkID(netWork.getNetworkID());
				pNetWorkDAO.addPNetWork(pNetWork);
				
				// 在NetWorkMeter里面为对应的父管网添加出水表
//				NetWorkMeter netWorkMeter = new NetWorkMeter();
//				netWorkMeter.setNetWorkID(pNetWork.getpNetWorkID());
//				netWorkMeter.setMeterID(pNetWork.getWAMeterID());
//				netWorkMeter.setMeterStyle(3);// 在DictionaryValue中3代表出水表
////				netWorkMeter.setOrderIndex(pNetWork.g);
//				netWorkMeter.setMeterType("WA");
//				netWorkMeterDAO.addNetWorkMeter(netWorkMeter);
			}
			for(NetWorkMeter netWorkMeter : netWorkMeterList){
				netWorkMeter.setNetWorkID(netWork.getNetworkID());
				netWorkMeterDAO.addNetWorkMeter(netWorkMeter);
			}
			transaction.commit(status);
		}catch(Exception e){
			logger.equals(e);
			logger.error("添加管网失败");
			transaction.rollback(status);
			return false;
		}
		return true;
	}
	
	/**
	 * 更新一条管网，并且把这个管网的父节点，配表信息都更新好
	 * @param netWork
	 * @param pNetWorkList
	 * @return
	 */
	public boolean updateNetWorkWithRelate(NetWork netWork,List<PNetWork> pNetWorkList,List<NetWorkMeter> netWorkMeterList){
//		TransactionStatus status = transaction.beginTansaction();
		try{
			pNetWorkDAO.deletePNetWorkByNetWorID(netWork.getNetworkID());
			netWorkMeterDAO.deleteNetWorkMeterByNetWorkID(netWork.getNetworkID());
//			netWorkDAO.addNetWork(netWork);
			for(PNetWork pNetWork : pNetWorkList){
				pNetWork.setNetWorkID(netWork.getNetworkID());
				pNetWorkDAO.addPNetWork(pNetWork);
				
				// 在NetWorkMeter里面为对应的父管网添加出水表
				NetWorkMeter netWorkMeter = new NetWorkMeter();
				netWorkMeter.setNetWorkID(pNetWork.getpNetWorkID());
				netWorkMeter.setMeterID(pNetWork.getWAMeterID());
				netWorkMeter.setMeterStyle(3);// 在DictionaryValue中3代表出水表
//				netWorkMeter.setOrderIndex(pNetWork.g);
				netWorkMeter.setMeterType("WA");
				netWorkMeterDAO.addNetWorkMeter(netWorkMeter);
			}
			for(NetWorkMeter netWorkMeter : netWorkMeterList){
				netWorkMeter.setNetWorkID(netWork.getNetworkID());
				netWorkMeterDAO.addNetWorkMeter(netWorkMeter);
			}
//			transaction.commit(status);
		}catch(Exception e){
			logger.equals(e);
			logger.error("添加管网失败");
//			transaction.rollback(status);
			return false;
		}
		return true;
	}
	
	public boolean deletePNetWorkByNetWorkIDAndPNetWorkID(int pNetWorkID, int netWorkID){
		int effectCount = pNetWorkDAO.deletePNetWorkByNetWorkIDAndPNetWorkID(pNetWorkID, netWorkID);
		if(effectCount == 0){
			return false;
		}
		return true;
	}
	
	public boolean deleteNetWorkMeterByNetWorkMeterID(int netWorkMeterID){
		List<Integer> oneNetWorkMeterIDList = new ArrayList<Integer>();
		oneNetWorkMeterIDList.add(netWorkMeterID);
		int effectCount = netWorkMeterDAO.deleteNetWorkMeterByNetWorkMeterIDs(oneNetWorkMeterIDList);
		if(effectCount == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 根据netWorkNum查询NetWork
	 * @param netWorkNum
	 * @return
	 */
	public NetWork getNetWorkByNetWorkNum(String netWorkNum){
		return netWorkDAO.getNetWorkByNetWorkNum(netWorkNum);
	}
	
	public List<Map<String,Object>> getPNetWorkByNetWorkID(int netWorkID){
		return pNetWorkDAO.getPNetWorkByNetWorkID(netWorkID);
	}
	
	public List<Map<String,Object>> getNetWorkMeterByNetWorkID(int netWorkID){
		return netWorkMeterDAO.getNetWorkMeterListByNetWorkID(netWorkID);
	}
	
	/**
	 * 通过areaID查询到Area
	 * @param areaID
	 * @return
	 */
	public Area getAreaByAreaID(int areaID){
		return areaDAO.getAreaByAreaID(areaID);
	}
	
	/**
	 * 根据meterIDs查找Meters
	 * @param meterIDs
	 * @return
	 */
	public List<Meter> getMeterByMeterIDs(List<Integer> meterIDs){
		return meterDAO.getMeterByMeterIDs(meterIDs);
	}
	
	/**
	 * 添加PNetWork一条数据，还需要把PNetWork里面的每一个meter在NetWorkMeter里面添加一行数据
	 * @throws Exception 
	 */
	/*public void addPNetWork(PNetWork pNetWork) throws Exception{
		// 添加PNetWork一条数据
		int count = pNetWorkDAO.addPNetWork(pNetWork);
		if(count == 0){
			throw new Exception("添加pNetWork失败");
		}
		// 把PNetWork里面的每一个meter在NetWorkMeter里面添加一行数据
		NetWorkMeter netWorkMeter = null;
		if(pNetWork.getWAMeterID() != null){
			netWorkMeter = new NetWorkMeter();
			netWorkMeter.setNetWorkID(pNetWork.getNetWorkID());
			netWorkMeter.setMeterID(pNetWork.getWAMeterID());
			netWorkMeter.setMeterStyle(1);// 这里1代表入水表，从pNetWork表里面同步添加到NetWorkMeter里面的表全是入水表，具体1代表的入水表，是从DictionaryValue表里面看的
			netWorkMeter.setMeterType("WA");
			netWorkMeterDAO.addNetWorkMeter(netWorkMeter);
		}
		
		if(pNetWork.getFLMeterID() != null){
			
		}
	}*/
}
