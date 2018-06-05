package com.sf.service;

import com.sf.bean.DictionaryValue;
import com.sf.bean.Meter;
import com.sf.bean.TexingValue;
import com.sf.commonbase.Transaction;
import com.sf.dao.DictionaryValueDAO;
import com.sf.dao.MeterDAO;
import com.sf.dao.TexingValueDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("meterService")
@Lazy(value = true)
public class MeterService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MeterDAO meterDAO;

	@Autowired
	private DictionaryValueDAO dictionaryValueDAO;

	@Autowired
	private TexingValueDAO texingValueDAO;
	
	// 事务管理
	@Autowired
	private Transaction txManager;
	
	/**
	 * 新增一个meter设备，并且设置这个设备的所有特性
	 * int meterTexingID,String texingValueText
	 * @param meter
	 * @return
	 */
	public boolean addMeterWithTexingValue(Meter meter,Map<Integer,String> texingMap){
		TransactionStatus status = txManager.beginTansaction();
		try{
			meterDAO.addMeter(meter);
			for(Integer meterTexingID : texingMap.keySet()) {
				TexingValue texingValue = new TexingValue();
				texingValue.setMeterID(meter.getMeterID());
				texingValue.setMeterTexingID(meterTexingID);
				texingValue.setTexingValue(texingMap.get(meterTexingID));
				texingValueDAO.addTexingValue(texingValue);
			}
			txManager.commit(status);
			return true;
		}catch(Exception e){
			logger.error(e.toString());
			logger.error("插入Meter失败");
			txManager.rollback(status);
			return false;
		}
	}
	
	/**
	 * 更新一个meter设备，并且设置这个设备的所有特性
	 * int meterTexingID,String texingValueText
	 * @param meter
	 * @return
	 */
	public boolean updateMeterWithTexingValue(Meter meter,Map<Integer,String> texingMap){
		TransactionStatus status = txManager.beginTansaction();
		try{
			meterDAO.updateMeter(meter);
			for(Integer meterTexingID : texingMap.keySet()) {
				TexingValue texingValue = new TexingValue();
				texingValue.setMeterID(meter.getMeterID());
				texingValue.setMeterTexingID(meterTexingID);
				texingValue.setTexingValue(texingMap.get(meterTexingID));
				texingValueDAO.updateTexingValue(texingValue);
			}
			txManager.commit(status);
			return true;
		}catch(Exception e){
			logger.error(e.toString());
			logger.error("插入Meter失败");
			txManager.rollback(status);
			return false;
		}
	}
	
	/**
	 * 根据参数查询DictionaryValue对象
	 * @param dictionaryID
	 * @return
	 */
	public List<DictionaryValue> getDictionaryValueByDictionaryID(int dictionaryID){
		List<DictionaryValue> dictionaryValueList = dictionaryValueDAO.getDictionaryValueByDictionaryID(dictionaryID);
		return dictionaryValueList;
	}
	
	public List<Map<String,Object>> getTypeAndValueByDictionaryValueNum(String dictionaryValueNum){
		List<Map<String, Object>> typeAndValueByDictionaryValueNum = dictionaryValueDAO.getTypeAndValueByDictionaryValueNum(dictionaryValueNum);
		return typeAndValueByDictionaryValueNum;
	}

	public List<Map<String,Object>> getTypeAndValueByDictionaryValueNumAndMeterID(String dictionaryValueNum,Integer meterID){
		List<Map<String, Object>> typeAndValueByDictionaryValueNum = dictionaryValueDAO.getTypeAndValueByDictionaryValueNumAndMeterID(dictionaryValueNum, meterID);
		return typeAndValueByDictionaryValueNum;
	}
	/**
	 * 根据meterNum删除设备
	 * @param meterIDs
	 */
	public boolean deleteMeterByMeterIDs(String[] meterIDs) {
		// 删除影响行数
		int effectCount = 0;
		TransactionStatus status = txManager.beginTansaction();
		try{
//			List<Integer> meterIDs = meterDAO.getMeterIDByMeterNums(meterNums);
			effectCount = meterDAO.deleteMeterByMeterIDs(meterIDs);
			for(String meterID : meterIDs){
				int meterIDInt = Integer.parseInt(meterID);
				texingValueDAO.deleteTexingValueByMeterID(meterIDInt);
			}
			txManager.commit(status);
		}catch(Exception e){
			logger.error(e.toString());
			logger.error("删除设备Meter出错meterIDs->"+meterIDs);
			txManager.rollback(status);
			return false;
		}
		// 删除成功
		if(effectCount != 0){
			return true;
		}
		// 删除失败
		return false;
	}

	/**
	 * 根据meterNum查找Meter
	 * @param meterNum
	 * @return
	 */
	public Meter getMeterByMeterNum(String meterNum) {
		return meterDAO.getMeterByMeterNum(meterNum);
	}
	
	public List<String> getColums(){
		return meterDAO.getColums();
	}

	public Map<String,Object> getMetersByFuzzy(String searchCondition, int limit,
			int current,String sortName,String sortType) {
		// 组装Meter表所有列模糊查询条件
		String search = "'%" + searchCondition + "%'"; 
		String like = " like ";
		String or = " or ";
		StringBuffer sb = new StringBuffer();
		List<String> meterColums = getColums();
		for(String colunm : meterColums){
			sb.append(colunm).append(like).append(search).append(or);
		}
		sb = sb.delete(sb.length() - or.length(), sb.length());
		
		int start = ((current-1) * limit) + 1;
		int end = current * limit;
		
		logger.info("模糊查询where->"+sb.toString());
		logger.info("模糊查询start->"+start);
		logger.info("模糊查询end->"+end);
		
		List<Meter> meters = meterDAO.getMetersByWhereLimit(start,end,sb.toString(),
				sortName,sortType);
		int totalCount = meterDAO.getAllCountByWhere(sb.toString());
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("meters", meters);
		retMap.put("totalCount", totalCount);
		return retMap;
	}
	
	public boolean setMeterDefaultValue(int meterID,int meterTexingID,String texingValueText){
		TexingValue texingValue = new TexingValue();
		texingValue.setMeterID(meterID);
		texingValue.setMeterTexingID(meterTexingID);
		texingValue.setTexingValue(texingValueText);
		try{
			texingValueDAO.addTexingValue(texingValue);
		}catch(Exception e){
			logger.error(e.toString());
			logger.error("插入TexingValue失败");
			return false;
		}
		return true;
	}
	
	public List<Meter> getMeterByMeterTypeWithLimitSizeAndFuzzy(int limitSize,
			String meterType, 
			String meterNum, 
			String meterName){
		return meterDAO.getMeterByMeterTypeWithLimitSizeAndFuzzy(limitSize, meterType, meterNum, meterName);
	}
	
	
	public List<Meter> getMeterWithLimitSizeAndFuzzy(int limitSize,
			String meterNum, 
			String meterName){
		return meterDAO.getMeterWithLimitSizeAndFuzzy(limitSize, meterNum, meterName);
	}
}
