package com.sf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import com.sf.bean.Area;
import com.sf.bean.ErrList;
import com.sf.bean.IndexPageMeter;
import com.sf.bean.Meter;
import com.sf.bean.NetWork;
import com.sf.bean.NetWorkMeter;
import com.sf.bean.T_AreaDay;
import com.sf.bean.T_WADay;
import com.sf.commonbase.Transaction;
import com.sf.dao.AreaDAO;
import com.sf.dao.DictionaryValueDAO;
import com.sf.dao.ErrListDAO;
import com.sf.dao.IndexPageMeterDAO;
import com.sf.dao.MeterDAO;
import com.sf.dao.NetWorkDAO;
import com.sf.dao.NetWorkMeterDAO;
import com.sf.dao.PNetWorkDAO;
import com.sf.dao.T_AreaDayDAO;
import com.sf.dao.T_WADayDAO;
import com.sf.dao.TexingValueDAO;
import com.sf.dao.ZDAO;


@Service("indexService")
@Lazy(value = true)
public class IndexService {
	
	private Logger logger = Logger.getLogger(getClass());
	
	// 事务管理
	@Autowired
	private Transaction txManager;

	@Autowired
	private NetWorkDAO netWorkDAO;

	@Autowired
	private AreaDAO areaDAO;

	@Autowired
	private NetWorkMeterDAO netWorkMeterDAO;

	@Autowired
	private T_WADayDAO t_WADayDAO;

	@Autowired
	private MeterDAO meterDAO;
	
//	@Resource(name="pNetWorkDAO")
	@Autowired
	private PNetWorkDAO pNetWorkDAO;

	@Autowired
	private DictionaryValueDAO dictionaryValueDAO;

	@Autowired
	private ZDAO zDAO;

	@Autowired
	private TexingValueDAO texingValueDAO;

	@Autowired
	private IndexPageMeterDAO indexPageMeterDAO;

	@Autowired
	private T_AreaDayDAO t_AreaDayDAO;

	@Autowired
	private ErrListDAO errListDAO;
	/**
	 *根据地区ID查询下级地区的个数
	 * @param area_ID
	 * @return
	 */
	public int getSubordinateRegion(Integer Area_ID) {
		return netWorkDAO.getSubordinateRegion(Area_ID);
	}

	/**
	 *根据地区ID查询下级地区管道的个数
	 * @param area_ID
	 * @return
	 */
	public int getAreaNetWork(Integer Area_ID) {
		return netWorkDAO.getAreaNetWork(Area_ID);
	}

	/**
	 * 根据地区ID查询 某种设备的个数
	 * @param areaID
	 * @param meterType 如果为null，则查询所有设备的个数
	 * @return
	 */
	public int getMeterCountByAreaIDAndMeterType(int areaID,String meterType) {
		return  netWorkDAO.getMeterCountByAreaIDAndMeterType(areaID,meterType);
	}
	
	/**
	 * 根据地区ID查询 某种设备
	 * @param areaID
	 * @param meterType 如果为null，则查询所有设备
	 * @return
	 */
	public List<Meter> getMeterByAreaIDAndMeterType(int areaID,String meterType){
		return netWorkDAO.getMeterByAreaIDAndMeterType(areaID, meterType);
	}
	
	/**
	 * 根据netWorkID 查找NetWork
	 * @param netWorkID
	 * @return
	 */
	public NetWork getNetWorkByNetWorkID(int netWorkID){
		List<Integer> netWorkIDList = new ArrayList<Integer>();
		netWorkIDList.add(netWorkID);
		return netWorkDAO.getNetWorkByNetWorkIDs(netWorkIDList).get(0);
	}
	
	/**
	 * 根据areaID 查找所有NetWork
	 * @param areaID
	 * @return
	 */
	public List<NetWork> getNetWorkByAreaID(int areaID){
		return netWorkDAO.getNetWorkByAreaID(areaID);
	}

	public List<Area> getAreasBySuperAreaID(int superAreaID){
		return areaDAO.getAreasBySuperiorAreaID(superAreaID);
	}
	
	public List<NetWorkMeter> getNetWorkMeterByNetWorkID(int netWorkID){
		return netWorkMeterDAO.getNetWorkMeterByNetWorkID(netWorkID);
	}
	
	public List<T_WADay> getT_WADayBetween(List<Integer> meterIDList,String startDateStr,String endDateStr){
		List<T_WADay> t_WADayList = t_WADayDAO.getT_WADayBetween(meterIDList, startDateStr, endDateStr);
		return t_WADayList;
	}
	
	public Meter getMeterByMeterID(int meterID){
		return meterDAO.getMeterByMeterID(meterID);
	}
	
	public List<Meter> getMeterByMeterType(String meterType){
		return meterDAO.getMeterByMeterType(meterType);
	}
	
	/**
	 * 查询FL表里面的数据
	 * @param meterID
	 * @param start
	 * @param end
	 * @param tableSuffixArr
	 * @return
	 */
	public List<Map<String,Object>> getPRByMeterIDBetweenStartAndEnd(int meterID,
			Date start,Date end,String[] tableSuffixArr){
		return zDAO.getPRByMeterIDBetweenStartAndEnd(meterID, start, end, tableSuffixArr);
	}
	
	/**
	 * 根据meterID查询这个设备的所有特性
	 * @param meterID
	 * @return
	 */
	public List<Map<String,Object>> getMeterTexingByMeterID(int meterID){
		return texingValueDAO.getMeterTexingByMeterID(meterID);
	}
	
	/**
	 * 添加一条IndexPageMeter记录
	 * @param indexPageMeter
	 * @return
	 */
	public boolean addIndexPageMeter(IndexPageMeter indexPageMeter){
		TransactionStatus status = txManager.beginTansaction();
		try{
			indexPageMeterDAO.addIndexPageMeter(indexPageMeter);
			txManager.commit(status);
			return true;
		}catch(Exception e){
			logger.error(e);
			logger.error("插入IndexPageMeter失败,请检查数据库连接情况");
			txManager.rollback(status);
			return false;
		}
	}
	
	/**
	 * 查询IndexPageMeter,传入的参数在IndexPageMeter，就是要找到这条记录,看下有没有
	 * @return
	 */
	public IndexPageMeter getIndexPageMeter(IndexPageMeter indexPageMeter){
		return indexPageMeterDAO.getIndexPageMeter(indexPageMeter);
	}
	
	/**
	 * 根据IndexPageMeter表里面的MeterID查找所有的设备
	 * @param indexPageMeterID
	 * @return
	 */
	public List<Meter> getMeterFromIndexPageMeterID(int areaID,String userID){
		return indexPageMeterDAO.getMeterFromIndexPageMeterID(areaID,userID);
	}
	
	/**
	 * 根据条件查询T_AreaDay数据，条件里面参数可以为空，例如，selectDay = null,
	 * 则代表查询的是到月的数据
	 * @param areaID
	 * @param selectYear
	 * @param selectMonth
	 * @param selectDay
	 * @return
	 */
	public Map<String, Object> getSumGrossInAndGrossOut(
			Integer areaID,Integer selectYear,Integer selectMonth,Integer selectDay){
		return t_AreaDayDAO.getSumGrossInAndGrossOut(areaID, selectYear, selectMonth, selectDay);
	}
	
	/**
	 * 根据AreaID和errType查询ErrList对象，条件里面可传入null
	 * @param areaID
	 * @param errType
	 * @return
	 */
	public List<Map<String, Object>> getErrListByAreaIDAndErrType(Integer areaID,String errType){
		return errListDAO.getErrListByAreaIDAndErrType(areaID, errType);
	}
	
	public List<T_AreaDay> getT_AreaDayBetween(List<Integer> areaIDList,
			String startDateStr,String endDateStr){
		return t_AreaDayDAO.getT_AreaDayBetween(areaIDList, startDateStr, endDateStr);
	}
}
