package com.sf.controller.systeminfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.bean.Area;
import com.sf.bean.DictionaryValue;
import com.sf.bean.Meter;
import com.sf.bean.NetWork;
import com.sf.bean.NetWorkMeter;
import com.sf.bean.PNetWork;
import com.sf.commonbase.JsonDateValueProcessor;
import com.sf.dao.DictionaryValueDAO;
import com.sf.service.MeterService;
import com.sf.service.NetWorkService;
import com.sf.utils.CommonUtils;
import com.sf.utils.Constant;
import com.sf.utils.SpringContextUtil;

@Controller
@RequestMapping(value = "/network", produces = "text/html;charset=UTF-8")
public class NetworkControllor {

	private Logger logger = Logger.getLogger(getClass());

	@Resource(name = "netWorkService")
	private NetWorkService netWorkService;


	@Resource(name = "meterService")
	private MeterService meterService;

	/**
	 * 返回管网档案信息json
	 * 
	 * @return
	 */
	@RequestMapping(value = "/networkInfo", method = RequestMethod.POST)
	@ResponseBody
	public String networkProfile(HttpServletRequest req, HttpServletResponse rsp)
			throws IOException {
		String limitRows = req.getParameter("currentSelectPageSize");
		String currentPage = req.getParameter("currentSelectPage");
		String sortName = req.getParameter("sortName");
		String sortType = req.getParameter("sortType");

		int limit = 0;
		int current = 0;
		try {
			limit = Integer.parseInt(limitRows);
			current = Integer.parseInt(currentPage);
		} catch (NumberFormatException e) {
			logger.error(e);
			logger.error("传入分页限制参数不为数字，limitRows->" + limitRows
					+ "，currentPage->" + currentPage + "，非法参数!");
			JSONObject json = new JSONObject();
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}

		if (limit == 0) {
			logger.error("传入分页限制为0，limitRows->" + limitRows + "，currentPage->"
					+ currentPage + "，非法参数!");
			JSONObject json = new JSONObject();
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}

		JSONObject json = new JSONObject();
		int totalCount = netWorkService.getAllCount();

		json.put(Constant.JSON_COUNT, totalCount);
		if (totalCount == 0) {
			// 查询结果为空时
			JSONArray netWorkInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, netWorkInfos);

			logger.debug("按照分页查询到netWork数据量为0，返回json->" + json.toString());
			return json.toString();
		}
		// 本页第一行和最后一行
		int start = ((current - 1) * limit) + 1;
		int end = current * limit;

		List<Map<String, Object>> netWorklist = netWorkService
				.getAllNetWorkByLimitPage(start, end,sortName,sortType);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		JSONArray netWorkInfos = JSONArray.fromObject(netWorklist, jsonConfig);
		json.put(Constant.JSON_ROWS, netWorkInfos);

		logger.debug("成功返回json->" + json.toString());
		return json.toString();
	}

	/**
	 * 插入
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/addNetWork", method = RequestMethod.POST)
	@ResponseBody
	public String addNetWork(HttpServletRequest req) {
		String _data = req.getParameter("_data");
		JSONObject data = null;
		try{
			data = JSONObject.fromObject(_data);
		}catch(Exception e){
			logger.error(e);
			logger.error("添加管网时addNetWork添加管网传入参数异常_data->"+_data);
			JSONObject json = new JSONObject();
			json.put(Constant.RESULT, Constant.FAILED);
			json.put(Constant.EFFECT_COUNT, 0);
			return json.toString();
		}
		
		String networkNum = data.getString("NetWork_Num");
		String networkName = data.getString("NetWork_Name");
		String areaID = data.getString("Area_ID");
		String pipeNetworkType = data.getString("PipeNetworkType");
		String networkCaliber = data.getString("NetWork_Caliber");
		String networkTotalLenght = data.getString("NetWork_TotalLenght");
		String enterWaterList = data.getString("EnterWaterList");
		String mapMeterList = data.getString("MapMeterList");
		
		int areaIDInt = 0;
		int pipeNetworkTypeInt = 0;
		Float networkTotalLenghtFloat = 0F;
		List<PNetWork> pNetWorkList = new ArrayList<PNetWork>();
		List<NetWorkMeter> netWorkMeterList = new ArrayList<NetWorkMeter>();
		
		JSONObject retJson = new JSONObject();
		// 检查并且获取参数
		try{
			areaIDInt = Integer.parseInt(areaID);
			pipeNetworkTypeInt = Integer.parseInt(pipeNetworkType);
			networkTotalLenghtFloat = Float.parseFloat(networkTotalLenght);
			
			JSONArray enterWaterListJsonArray = JSONArray.fromObject(enterWaterList);
			JSONArray mapMeterListJsonArray = JSONArray.fromObject(mapMeterList);
			// 从传入参数获取所有的入水表对象PNetWork
			Iterator enterWaterIterator = enterWaterListJsonArray.iterator();
			while(enterWaterIterator.hasNext()){
				JSONObject json = (JSONObject) enterWaterIterator.next();
				PNetWork pNetWork = new PNetWork();
				if(json.getString("PNetWork_ID") != null && !json.getString("PNetWork_ID").isEmpty()){
					pNetWork.setpNetWorkID(json.getInt("PNetWork_ID"));
				}
				if(json.getString("WA_Meter_ID") != null && !json.getString("WA_Meter_ID").isEmpty()){
					pNetWork.setWAMeterID(json.getInt("WA_Meter_ID"));
				}
				if(json.getString("FL_Meter_ID") != null && !json.getString("FL_Meter_ID").isEmpty()){
					pNetWork.setFLMeterID(json.getInt("FL_Meter_ID"));
				}
				if(json.getString("PR_Meter_ID") != null && !json.getString("PR_Meter_ID").isEmpty()){
					pNetWork.setPRMeterID(json.getInt("PR_Meter_ID"));
				}
				if(json.getString("Valve_Meter_ID") != null && !json.getString("Valve_Meter_ID").isEmpty()){
					pNetWork.setValveMeterID(json.getInt("Valve_Meter_ID"));
				}
				pNetWorkList.add(pNetWork);
			}
			// 从传入参数获取所有的配表对象NetWorkMeter
			Iterator mapMeterIterator = mapMeterListJsonArray.iterator();
			while(mapMeterIterator.hasNext()){
				JSONObject json = (JSONObject) mapMeterIterator.next();
				NetWorkMeter netWorkMeter = new NetWorkMeter();
				if(json.getString("NetWorkMeter_ID") != null && !json.getString("NetWorkMeter_ID").isEmpty()){
					netWorkMeter.setNetWorkMeterID(json.getInt("NetWorkMeter_ID"));
				}else{
					netWorkMeter.setNetWorkMeterID(null);
				}
				if(json.getString("Meter_ID") != null && !json.getString("Meter_ID").isEmpty()){
					netWorkMeter.setMeterID(json.getInt("Meter_ID"));
				}
				if(json.getString("MeterStyle") != null && !json.getString("MeterStyle").isEmpty()){
					netWorkMeter.setMeterStyle(json.getInt("MeterStyle"));
				}
				if(json.getString("OrderIndex") != null && !json.getString("OrderIndex").isEmpty()){
					netWorkMeter.setOrderIndex(json.getInt("OrderIndex"));
				}
				if(json.getString("Meter_Type") != null && !json.getString("Meter_Type").isEmpty()){
					netWorkMeter.setMeterType(json.getString("Meter_Type"));
				}
				netWorkMeterList.add(netWorkMeter);
			}
		}catch(Exception e){
			logger.error(e);
			logger.error("添加管网时addNetWork添加管网传入参数异常enterWaterList->"+enterWaterList
					+";mapMeterList->"+mapMeterList+";areaID->"+areaID
					+";pipeNetworkType->"+pipeNetworkType);
			JSONObject json = new JSONObject();
			json.put(Constant.RESULT, Constant.FAILED);
			json.put(Constant.EFFECT_COUNT, 0);
			return json.toString();
		}
		
		// 从传入参数获取所有的管网表对象NetWork
		NetWork netWork = new NetWork();
		netWork.setNetworkNum(networkNum);
		netWork.setNetworkName(networkName);
		netWork.setAreaID(areaIDInt);
		netWork.setPipeNetworkType(pipeNetworkTypeInt);
		netWork.setNetworkCaliber(networkCaliber);
		netWork.setNetworkTotalLenght(networkTotalLenghtFloat);
		
		JSONObject json = new JSONObject();
		boolean result = false;
		NetWork netWorkByNetWorkNum = netWorkService.getNetWorkByNetWorkNum(networkNum);
		if(netWorkByNetWorkNum != null){
			// 现在是更新NetWork，而不是添加
			result = netWorkService.updateNetWorkWithRelate(netWorkByNetWorkNum, pNetWorkList, netWorkMeterList);
		}else{
			// 现在是更新管网
//			for (NetWorkMeter netWorkMeter : netWorkMeterList) {
//				if(netWorkMeter.getNetWorkMeterID() == null){
//					
//				}
//			}
			Iterator<NetWorkMeter> iterator = netWorkMeterList.iterator();
			while(iterator.hasNext()){
				NetWorkMeter next = iterator.next();
				if(next.getNetWorkMeterID() == null){
					iterator.remove();
				}
			}
			result = netWorkService.addNetWorkWithRelate(netWork, pNetWorkList, netWorkMeterList);
		}
		
		if(result){
			json.put(Constant.RESULT, Constant.SUCCESS);
			json.put(Constant.EFFECT_COUNT, 1);
			return json.toString();
		}
		
		json.put(Constant.RESULT, Constant.FAILED);
		json.put(Constant.EFFECT_COUNT, 0);
		return json.toString();
	}

	/**
	 * 批量删除
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/deleteNetWorks", method = RequestMethod.POST)
	@ResponseBody
	public String deleteNetWorks(HttpServletRequest req) {
		String netWorkIDs = req.getParameter("NetWork_ID");
		String[] netWorkIDsArray = netWorkIDs.split(",");

		if (netWorkIDsArray == null) {
			logger.error("传入数据为null，NetWork_Num->" + netWorkIDsArray + "非法参数!");
			JSONObject json = new JSONObject();
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}

		boolean result = netWorkService.deleteNetWorks(netWorkIDsArray);
		JSONObject json = new JSONObject();
		if(result){
			json.put(Constant.RESULT, Constant.SUCCESS);
			json.put(Constant.EFFECT_COUNT, netWorkIDsArray.length);
			json.put(Constant.JSON_ROWS, "删除成功");
			return json.toString();
		}
		
		json.put(Constant.RESULT, Constant.FAILED);
		json.put(Constant.EFFECT_COUNT, 0);
		return json.toString();
	}

	/**
	 * 单独查询:查询管道以及下属各类设备
	 * 
	 * @param req
	 * @param rsp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/selectPipeNetwork", method = RequestMethod.POST)
	@ResponseBody
	public String selectMeterTypes(HttpServletRequest req,
			HttpServletResponse rsp) throws IOException {
		String SearchCode = req.getParameter("SearchCode");
		String SearchName = req.getParameter("SearchName");
		
		if(SearchCode != null && SearchCode.isEmpty()){
			SearchCode = null;
		}
		if(SearchName != null && SearchName.isEmpty()){
			SearchName = null;
		}
		
		String SearchTypes = req.getParameter("SearchType");
		String CurrentSelectSizes = req.getParameter("CurrentSelectSize");
		int SearchType = 0;
		int CurrentSelectSize = 0;
		try {
			SearchType = Integer.parseInt(SearchTypes);
			CurrentSelectSize = Integer.parseInt(CurrentSelectSizes);
		} catch (NumberFormatException e) {
			logger.error(e);
			logger.error("传入参数不为数字，SearchCode->" + SearchType
					+ "，currentPage->" + SearchType + "，非法参数!");
			return "";
		}
		
		JSONArray jsonArray = new JSONArray();
		// 这里跟前端约定:
		// SearchType==1查询入水管道
		// SearchType==2查询Meter表里面的 水表
		// SearchType==3查询Meter表里面的 压力机
		// SearchType==4查询Meter表里面的 流量计
		// SearchType==5查询Meter表里面的 阀门
		// SearchType==6查询
		switch (SearchType) {
		case 1:
			List<Map<String, Object>> netWorklist1 = netWorkService
					.getNetWorkByLimiteSizeAndFuzzy(CurrentSelectSize,
							SearchCode, SearchName);
			jsonArray = JSONArray.fromObject(netWorklist1);
			break;
		case 2:
//			List<Map<String, Object>> netWorklist2 = meterService
//					.selectNetWorkCurrentSelectWASize(CurrentSelectSize,
//							SearchCode, SearchName);
			List<Meter> waMeters = meterService.getMeterByMeterTypeWithLimitSizeAndFuzzy(CurrentSelectSize, 
					Constant.METER_TYPE.WA.toString(), SearchCode, SearchName);
			for(Meter meter : waMeters){
				JSONObject json = new JSONObject();
				json.put("ID", meter.getMeterID());
				json.put("CODE", meter.getMeterNum());
				json.put("NAME", meter.getMeterName());
				jsonArray.add(json);
			}
			break;
		case 3:
			List<Meter> prMeters = meterService.getMeterByMeterTypeWithLimitSizeAndFuzzy(CurrentSelectSize, 
					Constant.METER_TYPE.PR.toString(), SearchCode, SearchName);
			for(Meter meter : prMeters){
				JSONObject json = new JSONObject();
				json.put("ID", meter.getMeterID());
				json.put("CODE", meter.getMeterNum());
				json.put("NAME", meter.getMeterName());
				jsonArray.add(json);
			}
			break;
		case 4:
			List<Meter> flMeters = meterService.getMeterByMeterTypeWithLimitSizeAndFuzzy(CurrentSelectSize, 
					Constant.METER_TYPE.FL.toString(), SearchCode, SearchName);
			for(Meter meter : flMeters){
				JSONObject json = new JSONObject();
				json.put("ID", meter.getMeterID());
				json.put("CODE", meter.getMeterNum());
				json.put("NAME", meter.getMeterName());
				jsonArray.add(json);
			}
			break;
		case 5:
			List<Meter> valveMeters = meterService.getMeterByMeterTypeWithLimitSizeAndFuzzy(CurrentSelectSize, 
					Constant.METER_TYPE.VAlVE.toString(), SearchCode, SearchName);
			for(Meter meter : valveMeters){
				JSONObject json = new JSONObject();
				json.put("ID", meter.getMeterID());
				json.put("CODE", meter.getMeterNum());
				json.put("NAME", meter.getMeterName());
				jsonArray.add(json);
			}
			break;
		case 6:
			if(SearchCode != null && SearchCode.equals("")) SearchCode = null;
			if(SearchName != null && SearchName.equals("")) SearchName = null;
			List<Meter> meters = meterService.getMeterWithLimitSizeAndFuzzy(CurrentSelectSize, SearchCode, SearchName);
			for(Meter meter : meters){
				JSONObject json = new JSONObject();
				json.put("ID", meter.getMeterID());
				json.put("CODE", meter.getMeterNum());
				json.put("NAME", meter.getMeterName());
				jsonArray.add(json);
			}
			break;

		default:
			logger.error("传入ID不在合理范围，SearchType->" + SearchType + "，非法参数!");
			break;
		}

		return jsonArray.toString();
	}

	/**
	 * 单独查询:查询NetWork所有管道
	 * 
	 * @param req
	 * @param rsp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/selectNetWorkCurrentSelectSize", method = RequestMethod.POST)
	@ResponseBody
	public String selectNetWorkCurrentSelectPage(HttpServletRequest req,
			HttpServletResponse rsp) throws IOException {
		String currentSelectSizes = req.getParameter("currentSelectSize");

		int currentSelectSize = 0;

		JSONObject json = new JSONObject();
		try {
			currentSelectSize = Integer.parseInt(currentSelectSizes);
		} catch (NumberFormatException e) {
			logger.error(e);
			logger.error("传入currentSelectPage参数不为数字，currentSelectPage->"
					+ currentSelectSize + "，非法参数!");

			JSONArray netWorkInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, netWorkInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}

		List<Map<String, Object>> netWorklist = netWorkService.getNetWorkByLimiteSize(currentSelectSize);
		// 添加返回数据
		JSONArray jsonArray = JSONArray.fromObject(netWorklist);
		return jsonArray.toString();

	}

	/**
	 * 单独查询:根据管道类型值查询
	 * 
	 * @param req
	 * @param rsp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/selectNetWorkType", method = RequestMethod.POST)
	@ResponseBody
	public String cxNetWorks(HttpServletRequest req, HttpServletResponse rsp)
			throws IOException {

		String Dictionary_IDRows = req.getParameter("Dictionary_ID");

		int Dictionary_ID = 0;
		try {
			Dictionary_ID = Integer.parseInt(Dictionary_IDRows);
		} catch (NumberFormatException e) {
			logger.error(e);
			logger.error("传入ID参数不为数字，Dictionary_ID->" + Dictionary_ID
					+ "，非法参数!");
			JSONObject json = new JSONObject();
			JSONArray netWorkInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, netWorkInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}

		List<DictionaryValue> dictionaryValueList = netWorkService
				.getDictionaryValueByDictionaryID(Dictionary_ID);

		JSONArray jsonArray = new JSONArray();

		for (DictionaryValue dictionaryValue : dictionaryValueList) {
			JSONObject json = new JSONObject();
			json.put("DictionaryValue_Num",
					dictionaryValue.getDictionaryValueNum());
			json.put("DictionaryValue_Value",
					dictionaryValue.getDictionaryValueValue());
			jsonArray.add(json);
		}

		logger.debug("成功返回json->" + jsonArray.toString());
		return jsonArray.toString();
	}

	/**
	 * 查询:根据字段属性模糊查询
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/selectNetWorks", method = RequestMethod.POST)
	@ResponseBody
	public String selectNetWorks(HttpServletRequest req) {
		JSONObject json = new JSONObject();
		String NetWork_Num = req.getParameter("NetWork_Num");
		String NetWork_Name = req.getParameter("NetWork_Name");
		String Area_ID = req.getParameter("Area_ID");
		String PipeNetworkType = req.getParameter("PipeNetworkType");
		String NetWork_Caliber = req.getParameter("NetWork_Caliber");
		String NetWork_TotalLenght = req.getParameter("NetWork_TotalLenght");
		String currentPage = req.getParameter("currentSelectPage");
		String limitRows = req.getParameter("currentSelectPageSize");
		String sortName = req.getParameter("sortName");
		String sortType = req.getParameter("sortType");

		// 参数检查
		if(currentPage == null || currentPage.isEmpty() ||
				limitRows == null || limitRows.isEmpty() ||
				sortName == null || sortName.isEmpty() ||
				sortType == null || sortType.isEmpty()){
			return CommonUtils.getFailJson().toString();
		}
		// 初始化参数，如果传过来的为空，则代表不根据这个字段进行查询，直接赋值为null，则sql不会进行查询
		if(NetWork_Num == null || NetWork_Num.isEmpty()){
			NetWork_Num = null;
		}
		if(NetWork_Name == null || NetWork_Name.isEmpty()){
			NetWork_Name = null;
		}
		if(Area_ID == null || Area_ID.isEmpty()){
			Area_ID = null;
		}
		if(PipeNetworkType == null || PipeNetworkType.isEmpty()){
			PipeNetworkType = null;
		}
		if(NetWork_Caliber == null || NetWork_Caliber.isEmpty()){
			NetWork_Caliber = null;
		}
		if(NetWork_TotalLenght == null || NetWork_TotalLenght.isEmpty()){
			NetWork_TotalLenght = null;
		}
		
		int limit = 0;
		int current = 0;
		try {
			limit = Integer.parseInt(limitRows);
			current = Integer.parseInt(currentPage);
		} catch (NumberFormatException e) {
			logger.error(e);
			logger.error("传入分页限制参数不为数字，limitRows->" + limitRows
					+ "，currentPage->" + currentPage + "，非法参数!");
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}
		// 本页第一行和最后一行
		int start = ((current - 1) * limit) + 1;
		int end = current * limit;
		List<Map<String, Object>> list = netWorkService.selectNetWorkss(
				NetWork_Num, NetWork_Name, PipeNetworkType, NetWork_Caliber,
				NetWork_TotalLenght, Area_ID,start,end,sortName,sortType);
		// 类型转换将时间字段空值转换成 " "
		for (Map<String, Object> map : list) {
			if(map.get("NetWork_Num") == null){
				map.put("NetWork_Num","");
			}
			if(map.get("Area_ID") == null){
				map.put("Area_ID","");
			}
			if(map.get("LastMissedTime") == null){
				map.put("LastMissedTime","");
			}
			if(map.get("Area_Name") == null){
				map.put("Area_Name","");
			}
			
//			map.put("NetWork_Num",
//					map.get("NetWork_Num") == null ? " " : (map
//							.get("NetWork_Num")));
//			map.put("Area_ID",
//					map.get("Area_ID") == null ? 0 : (map.get("Area_ID")));
//			map.put("LastMissedTime", map.get("LastMissedTime") == null ? " "
//					: (map.get("LastMissedTime")));
		}
		// 返回行数
		int totalCount = netWorkService.getAllCounts(NetWork_Num, NetWork_Name,
				PipeNetworkType, NetWork_Caliber, NetWork_TotalLenght, Area_ID);
		json.put(Constant.JSON_COUNT, totalCount);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		// 添加返回数据
		JSONArray netWorkInfos = JSONArray.fromObject(list, jsonConfig);
		json.put(Constant.JSON_ROWS, netWorkInfos);
		logger.debug("成功返回json->" + json.toString());
		return json.toString();
	}
	
	/**
	 * 编辑页面删除PNetWork
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/deletePNetWork", method = RequestMethod.POST)
	@ResponseBody
	public String deletePNetWork(HttpServletRequest req) {
		String pNetWorkID = req.getParameter("pNetWorkID");
		String netWorkID = req.getParameter("netWorkID");
		int pNetWorkIDInt = 0;
		int netWorkIDInt = 0;
		try{
			pNetWorkIDInt = Integer.parseInt(pNetWorkID);
			netWorkIDInt = Integer.parseInt(netWorkID);
		}catch(Exception e){
			logger.error(e);
			logger.error("编辑页面删除PNetWork传入参数异常pNetWorkID->"+pNetWorkID
					+";netWorkID->"+netWorkID);
			JSONObject json = new JSONObject();
			json.put(Constant.RESULT, Constant.FAILED);
			json.put(Constant.EFFECT_COUNT, 0);
			return json.toString();
		}
		
		
		JSONObject json = new JSONObject();
		boolean result = netWorkService
				.deletePNetWorkByNetWorkIDAndPNetWorkID(pNetWorkIDInt, netWorkIDInt);
		if(result){
			json.put(Constant.RESULT, Constant.SUCCESS);
			json.put(Constant.EFFECT_COUNT, 1);
			return json.toString();
		}
		
		json.put(Constant.RESULT, Constant.FAILED);
		json.put(Constant.EFFECT_COUNT, 0);
		return json.toString();
	}
	
	/**
	 * 编辑页面删除PNetWork
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/deleteNetWorkMeter", method = RequestMethod.POST)
	@ResponseBody
	public String deleteNetWorkMeter(HttpServletRequest req) {
		String netWorkMeterID = req.getParameter("netWorkMeterID");
		int netWorkMeterIDInt = 0;
		try{
			netWorkMeterIDInt = Integer.parseInt(netWorkMeterID);
		}catch(Exception e){
			logger.error(e);
			logger.error("编辑页面删除PNetWork传入参数异常netWorkMeterID->"+netWorkMeterID);
			JSONObject json = new JSONObject();
			json.put(Constant.RESULT, Constant.FAILED);
			json.put(Constant.EFFECT_COUNT, 0);
			return json.toString();
		}
		JSONObject json = new JSONObject();
		boolean result = netWorkService
				.deleteNetWorkMeterByNetWorkMeterID(netWorkMeterIDInt);
		if(result){
			json.put(Constant.RESULT, Constant.SUCCESS);
			json.put(Constant.EFFECT_COUNT, 1);
			return json.toString();
		}
		
		json.put(Constant.RESULT, Constant.FAILED);
		json.put(Constant.EFFECT_COUNT, 0);
		return json.toString();
	}

	/**
	 * 打开编辑管网页面需要的管网数据
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getNetWorkDetail", method = RequestMethod.POST)
	@ResponseBody
	public String getNetWorkDetail(HttpServletRequest req) {
		String netWorkNum = req.getParameter("netWorkNum");
		// 返回的Json
		JSONObject retJson = new JSONObject();
		NetWork netWork = null;
		
		try{
			netWork = netWorkService.getNetWorkByNetWorkNum(netWorkNum);
		}catch(Exception e){
			logger.error(e);
			logger.error("根据netWorkNum查询管网数据库操作报错");
			return null;
		}
		
		if(netWork.getAreaID()==null){
			retJson.put("Area_ID", "");
			retJson.put("Area_Num", "");
			retJson.put("Area_Name", "");
		}else{
			retJson.put("Area_ID", netWork.getAreaID());
			Area area = netWorkService.getAreaByAreaID(netWork.getAreaID());
			retJson.put("Area_Num", area.getAreaNum() == null ? "" : area.getAreaNum());
			retJson.put("Area_Name", area.getAreaName() == null ? "" : area.getAreaName());
		}
		
		retJson.put("NetWork_Num", 
				netWork.getNetworkNum()==null ? "" : netWork.getNetworkNum());
		retJson.put("NetWork_ID", 
				netWork.getNetworkID()==null ? "" : netWork.getNetworkID());
		retJson.put("NetWork_Name", 
				netWork.getNetworkName()==null ? "" : netWork.getNetworkName());
		retJson.put("PipeNetworkType", 
				netWork.getPipeNetworkType()==null ? "" : netWork.getPipeNetworkType());
		retJson.put("NetWork_Caliber", 
				netWork.getNetworkCaliber()==null ? "" : netWork.getNetworkCaliber());
		retJson.put("NetWork_TotalLenght", 
				netWork.getNetworkTotalLenght()==null ? "" : netWork.getNetworkTotalLenght());
		
		List<Map<String,Object>> pNetWorkMapList = 
				netWorkService.getPNetWorkByNetWorkID(netWork.getNetworkID());
		for (Map<String, Object> map : pNetWorkMapList) {
			if(map.get("NetWork_ID") == null){
				map.put("NetWork_ID","");
			}
			if(map.get("PNetWork_ID") == null){
				map.put("PNetWork_ID","");
			}
			// 如果有入水信息里面有水表，记下来，等下查出来 放到
			if(map.get("WA_Meter_ID") == null){
				map.put("WA_Meter_ID","");
			}
			if(map.get("FL_Meter_ID") == null){
				map.put("FL_Meter_ID","");
			}
			if(map.get("PR_Meter_ID") == null){
				map.put("PR_Meter_ID","");
			}
			if(map.get("Valve_Meter_ID") == null){
				map.put("Valve_Meter_ID","");
			}
		}
		JSONArray enterWaterList = JSONArray.fromObject(pNetWorkMapList);
		retJson.put("EnterWaterList", enterWaterList);
		
		List<Map<String, Object>> netWorkMeterMapList = 
				netWorkService.getNetWorkMeterByNetWorkID(netWork.getNetworkID());
		for (Map<String, Object> map : netWorkMeterMapList) {
			if(map.get("NetWorkMeter_ID") == null){
				map.put("NetWorkMeter_ID","");
			}
			if(map.get("NetWork_ID") == null){
				map.put("NetWork_ID","");
			}
			if(map.get("Meter_ID") == null){
				map.put("Meter_ID","");
			}
			if(map.get("MeterStyle") == null){
				map.put("MeterStyle","");
			}
			if(map.get("OrderIndex") == null){
				map.put("OrderIndex","");
			}
			if(map.get("Meter_Type") == null){
				map.put("Meter_Type","");
			}
		}
		JSONArray mapMeterList = JSONArray.fromObject(netWorkMeterMapList);
		retJson.put("MapMeterList", mapMeterList);
		
		
		DictionaryValueDAO dictionaryValueDAO = SpringContextUtil.getBean("dictionaryValueDAO");
		// 遍历获取配表信息的所有
		for(Constant.ConfigMeterInfo e : Constant.ConfigMeterInfo.values()){
			List<DictionaryValue> dictionaryValueList = 
					dictionaryValueDAO.getDictionaryValueByDictionaryID(e.getDictionaryID());
			JSONArray jsonArray = new JSONArray();
			for(DictionaryValue dictionaryValue : dictionaryValueList){
				JSONObject json = new JSONObject();
				json.put("DictionaryValue_Num", dictionaryValue.getDictionaryValueNum());
				json.put("DictionaryValue_Value", dictionaryValue.getDictionaryValueValue());
				jsonArray.add(json);
			}
			
			if(e.toString().equals(Constant.ConfigMeterInfo.meterType.toString())){
				// 表计类型
				retJson.put("Meter_TypeList", jsonArray);
				continue;
			}
			if(e.toString().equals(Constant.ConfigMeterInfo.deviceType.toString())){
				// 设备类型
				retJson.put("MeterStyleList", jsonArray);
			}
			if(e.toString().equals(Constant.ConfigMeterInfo.netWrokType.toString())){
				// 管道类型
				retJson.put("PipeNetworkTypeList", jsonArray);
			}
		}
		
		return retJson.toString();
	}
}
