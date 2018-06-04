package com.sf.controller.systeminfo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
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
import com.sf.commonbase.JsonDateValueProcessor;
import com.sf.dao.DictionaryValueDAO;
import com.sf.dao.MeterDAO;
import com.sf.service.MeterService;
import com.sf.utils.Constant;

@Controller
@RequestMapping("/meter")
public class DeviceControllor {
	private Logger logger = Logger.getLogger(getClass());

	@Resource(name="meterDAO")
	private MeterDAO meterDAO;
	
	@Resource(name="dictionaryValueDAO")
	private DictionaryValueDAO dictionaryValueDAO;
	
	
	
	/**
	 * 返回设备档案信息json
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/meterInfo",method=RequestMethod.POST) 
	@ResponseBody
	public String deviceProfile(HttpServletRequest req) throws IOException{
		String limitRows = req.getParameter("currentSelectPageSize");
		String currentPage = req.getParameter("currentSelectPage");
		String sortName = req.getParameter("sortName");
		String sortType = req.getParameter("sortType");
		
		int limit = 0;
		int current = 0;
		try{
			limit = Integer.parseInt(limitRows);
			current = Integer.parseInt(currentPage);
		}catch(NumberFormatException e){
			logger.error(e);
			logger.error("传入分页限制参数不为数字，limitRows->"+limitRows+"，currentPage->"+currentPage+"，非法参数!");
			JSONObject json = new JSONObject();
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}
		
		if(limit == 0){
			logger.error("传入分页限制为0，limitRows->"+limitRows+"，currentPage->"+currentPage+"，非法参数!");
			JSONObject json = new JSONObject();
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}
		
		JSONObject json = new JSONObject();
		
		int totalCount = meterDAO.getAllCount();
		json.put(Constant.JSON_COUNT, totalCount);
		if(totalCount == 0){
			// 查询结果为空的数据
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			
			logger.debug("按照分页查询到meter数据量为0，返回json->"+json.toString());
			return json.toString();
		}
		
		int start = ((current-1) * limit) + 1;
		int end = current * limit;
		List<Meter> meterList = meterDAO.getAllMetersByLimitPageAndSort(start, end,sortName,sortType);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());
		JSONArray meterInfos = JSONArray.fromObject(meterList,jsonConfig);
		json.put(Constant.JSON_ROWS, meterInfos);
		
		logger.debug("成功返回json->"+json.toString());
		return json.toString();
	}
	
	@Resource(name="meterService")
	private MeterService meterService;
	
	/**
	 * 获取总共有多少种设备，如水表，电表......
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getMeterType",method=RequestMethod.POST) 
	@ResponseBody
	public String getMeterType(HttpServletRequest req){
		String dictionaryID = req.getParameter("Dictionary_ID");
		return getMeterType(dictionaryID);
	}
	
	public String getMeterType(String dictionaryID){
		int dictionaryIDInt = 0;
		try{
			dictionaryIDInt = Integer.parseInt(dictionaryID);
		}catch(Exception e){
			logger.error(e);
			logger.error("getMeterType请求参数异常:Dictionary_ID->"+dictionaryID);
		}
		
		List<DictionaryValue> dictionaryValueList = meterService.getDictionaryValueByDictionaryID(dictionaryIDInt);
		JSONArray jsonArray = new JSONArray();
		for(DictionaryValue dictionaryValue : dictionaryValueList){
			JSONObject json = new JSONObject();
			json.put("DictionaryValue_Num", dictionaryValue.getDictionaryValueNum());
			json.put("DictionaryValue_Value", dictionaryValue.getDictionaryValueValue());
			jsonArray.add(json);
		}
		return jsonArray.toString();
	}
	
	public String getMeterTypeByMeterID(String dictionaryID,Integer meterID){
		int dictionaryIDInt = 0;
		try{
			dictionaryIDInt = Integer.parseInt(dictionaryID);
		}catch(Exception e){
			logger.error(e);
			logger.error("getMeterType请求参数异常:Dictionary_ID->"+dictionaryID);
		}
		
		List<DictionaryValue> dictionaryValueList = meterService.getDictionaryValueByDictionaryID(dictionaryIDInt);
		JSONArray jsonArray = new JSONArray();
		for(DictionaryValue dictionaryValue : dictionaryValueList){
			JSONObject json = new JSONObject();
			json.put("DictionaryValue_Num", dictionaryValue.getDictionaryValueNum());
			json.put("DictionaryValue_Value", dictionaryValue.getDictionaryValueValue());
			jsonArray.add(json);
		}
		return jsonArray.toString();
	}
	
	
	
	/**
	 * 获取某种设备下面的所有特性，并且返回这些特性的默认值
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getMeterTexingByType",method=RequestMethod.POST) 
	@ResponseBody
	public String getMeterTexingByType(HttpServletRequest req){
		String dictionaryValueNum = req.getParameter("DictionaryValue_Num");
		return getMeterTexingByType(dictionaryValueNum);
	}
	
	public String getMeterTexingByType(String dictionaryValueNum){
		List<Map<String, Object>> typeAndValueList = meterService.getTypeAndValueByDictionaryValueNum(dictionaryValueNum);
		return getMeterTexingByTypeByList(typeAndValueList);
	}
	
	private String getMeterTexingByTypeAndMeterID(String dictionaryValueNum,Integer meterID){
		List<Map<String, Object>> typeAndValueList = meterService.getTypeAndValueByDictionaryValueNumAndMeterID(dictionaryValueNum, meterID);
		return getMeterTexingByTypeByList(typeAndValueList);
	}
	
	private String getMeterTexingByTypeByList(List<Map<String, Object>> typeAndValueList){
		Map<String,JSONObject> jsonObjectMap = new HashMap<String,JSONObject>();
		for(Map<String, Object> tempMap : typeAndValueList){
			String type = (String) tempMap.get("MeterTexing_ShowType");
			JSONObject json = jsonObjectMap.get(type);
			if(json == null){
				json = new JSONObject();
				json.put("ftype", type.toLowerCase());
				json.put("fid", type.toLowerCase()+"_"+String.valueOf(tempMap.get("MeterTexing_ID")));
				if(tempMap.get("TexingValue") != null){
					// 如果根据meterID查询后调用此方法，会存在默认值，不然不存在默认值
					json.put("defaultValue", tempMap.get("TexingValue") == null ? "" : tempMap.get("TexingValue"));
				}else{
					json.put("defaultValue", "");
				}
				json.put("defaultName", tempMap.get("MeterTexing_Name"));
				JSONArray jsonArray = new JSONArray();
				json.put("data", jsonArray);
				jsonObjectMap.put(type, json);
			}
			JSONArray dataJsonArray = JSONArray.fromObject(json.get("data").toString());
			JSONObject tempJson = new JSONObject();
			tempJson.put("DictionaryValue_Num", tempMap.get("DictionaryValue_Num"));
			tempJson.put("DictionaryValue_Value", tempMap.get("DictionaryValue_Value"));
			dataJsonArray.add(tempJson);
			json.put("data", dataJsonArray);

			jsonObjectMap.put(type, json);
		}
		
		JSONArray jsonArray = new JSONArray();
		for(String key : jsonObjectMap.keySet()){
			jsonArray.add(jsonObjectMap.get(key));
		}
		return jsonArray.toString();
	}
	
	/**
	 * 添加Meter设备,以及为这个设备设置它的相关特性值，如大小、宽度什么的
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/addMeter",method=RequestMethod.POST) 
	@ResponseBody
	public String addMeter(HttpServletRequest req){
		// 设备相关属性
		String meterNum = req.getParameter("meterNum");
		String meterName = req.getParameter("meterName");
		String meterType = req.getParameter("meterType");
		String meterChangjia = req.getParameter("meterChangjia");
		String meterTime = req.getParameter("meterTime");
		String meterRemark = req.getParameter("meterRemark");
		String meter485Address = req.getParameter("meter485Address");
		String beilv = req.getParameter("beilv");
		String xiuzheng = req.getParameter("xiuzheng");
		String meterValue1 = req.getParameter("meterValue1");
		String meterValue2 = req.getParameter("meterValue2");
		String meterValue3 = req.getParameter("meterValue3");
		String meterValue4 = req.getParameter("meterValue4");
		
		Meter meter = new Meter();
		// time
		try{
			if(meterTime != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
				Date meterTimeDate = sdf.parse(meterTime);
				meter.setMeterTime(meterTimeDate);
			}
		}catch(ParseException e){
			logger.error(e);
			logger.error("添加Meter，传入参数异常meterTime->"+meterTime);
			JSONObject json = new JSONObject();
			json.put(Constant.RESULT, Constant.FAILED);
			json.put(Constant.EFFECT_COUNT, 0);
			return json.toString();
		}
		
		try{
			if(beilv != null){
				Float beilvFloat = Float.parseFloat(beilv);
				meter.setBeilv(beilvFloat);
			}
			if(xiuzheng != null){
				Float xiuzhengFloat = Float.parseFloat(xiuzheng);
				meter.setXiuzheng(xiuzhengFloat);
			}
			if(meterValue1 != null){
				int meterValue1Int = Integer.parseInt(meterValue1);
				meter.setMeterValue1(meterValue1Int);
			}
			if(meterValue2 != null){
				int meterValue2Int = Integer.parseInt(meterValue2);
				meter.setMeterValue2(meterValue2Int);
			}
			if(meterValue3 != null){
				int meterValue3Int = Integer.parseInt(meterValue3);
				meter.setMeterValue3(meterValue3Int);
			}
			if(meterValue4 != null){
				int meterValue4Int = Integer.parseInt(meterValue4);
				meter.setMeterValue4(meterValue4Int);
			}
			
		}catch(NumberFormatException e){
			logger.error(e);
			logger.error("添加Meter，传入参数异常beilv->"+beilv+",xiuzheng->"+xiuzheng+",meterValue1->"+meterValue1+",meterValue2->"+meterValue2+",meterValue3->"+meterValue3+",meterValue4->"+meterValue4);
			JSONObject json = new JSONObject();
			json.put(Constant.RESULT, Constant.FAILED);
			json.put(Constant.EFFECT_COUNT, 0);
			return json.toString();
		}
		meter.setMeterNum(meterNum);
		meter.setMeterName(meterName);
		meter.setMeterType(meterType);
		meter.setMeterChangjia(meterChangjia);
		meter.setMeterRemark(meterRemark);
		meter.setMeter485Address(meter485Address);
		
		JSONObject json = new JSONObject();
		
		// 特性相关属性
		Map<Integer,String> texingMap = new HashMap<Integer,String>();
		// 1:DN30|2:1
		String asyList = req.getParameter("asyList");
		try{
			String[] texingArray = asyList.split("\\|");
			for(String tempTexingKeyValue : texingArray){
				String[] texingKeyValue = tempTexingKeyValue.split(":");
				texingMap.put(Integer.valueOf(texingKeyValue[0]), texingKeyValue[1]);
			}
		}catch(Exception e){
			logger.error(e);
			logger.error("添加设备Meter时参数asyList->"+asyList);
			// 这里修改，有的设备本身就没有特性，所以就算没有特性，也要成功添加设备,不失败
//			json.put(Constant.RESULT, Constant.FAILED);
//			json.put(Constant.EFFECT_COUNT, 0);
//			return json.toString();
		}
		boolean result = false;
		// 查询是否存在这个meter
		Meter meterByMeterNum = meterService.getMeterByMeterNum(meter.getMeterNum());
		if(meterByMeterNum != null){
			// 现在是执行更新Meter的操作
			meter.setMeterID(meterByMeterNum.getMeterID());
			result = meterService.updateMeterWithTexingValue(meter, texingMap);
		}else{
			result = meterService.addMeterWithTexingValue(meter, texingMap);
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
	
	@RequestMapping(value="/deleteMeters",method=RequestMethod.POST) 
	@ResponseBody
	public String deleteMeters(HttpServletRequest req){
		// TODO
		String meterIDList = req.getParameter("meterIDList");
		String[] meterIDs = meterIDList.split(",");
		boolean result = meterService.deleteMeterByMeterIDs(meterIDs);
		
		JSONObject json = new JSONObject();
		if(result){
			json.put(Constant.RESULT, Constant.SUCCESS);
			json.put(Constant.EFFECT_COUNT, meterIDs.length);
			return json.toString();
		}
		
		json.put(Constant.RESULT, Constant.FAILED);
		json.put(Constant.EFFECT_COUNT, 0);
		return json.toString();
	}
	
	/**
	 * 获取设备详细信息
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getMeterDetail",method=RequestMethod.POST) 
	@ResponseBody
	public String getMeterDetail(HttpServletRequest req){
		String meterNum = req.getParameter("meterNum");
		Meter meter = meterService.getMeterByMeterNum(meterNum);
		// 这里为了重用之前的代码
		String jsonText = getMeterTexingByTypeAndMeterID(meter.getMeterType(),meter.getMeterID());
		JSONArray jsonArray = JSONArray.fromObject(jsonText);
		
		JSONObject json = new JSONObject();
		json.put("texing", jsonArray);
		json.put("meterNum", meter.getMeterNum());
		if(meter.getMeterNum() == null){
			json.put("meterNum", "");
		}
		json.put("meterName", meter.getMeterName());
		if(meter.getMeterName() == null){
			json.put("meterName", "");
		}
//		json.put("meterType", meter.getMeterType());
//		if(meter.getMeterType() == null){
//			json.put("meterType", "");
//		}
		json.put("meterChangjia", meter.getMeterChangjia());
		if(meter.getMeterChangjia() == null){
			json.put("meterChangjia", "");
		}
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			String formatMeterTime = sdf.format(meter.getMeterTime());
			json.put("meterTime", formatMeterTime);
		}catch(Exception e){
			json.put("meterTime", "");
		}
		json.put("meterRemark", meter.getMeterRemark());
		if(meter.getMeterRemark() == null){
			json.put("meterRemark", "");
		}
		json.put("meter485Address", meter.getMeter485Address());
		if(meter.getMeter485Address() == null){
			json.put("meter485Address", "");
		}
		json.put("beilv", meter.getBeilv());
		if(meter.getBeilv() == null){
			json.put("beilv", "");
		}
		json.put("xiuzheng", meter.getXiuzheng());
		if(meter.getXiuzheng() == null){
			json.put("xiuzheng", "");
		}
		
		JSONObject meterTypeJson = new JSONObject();
		if(meter.getMeterType() == null){
			meterTypeJson.put("meterType", "");
			JSONArray tempJsonArray = new JSONArray();
			meterTypeJson.put("meterTypeList", tempJsonArray);
		}else{
			meterTypeJson.put("meterType", meter.getMeterType());
			DictionaryValue dictionaryValue = dictionaryValueDAO.getDictionaryValueByDictionaryValueNum(meter.getMeterType());
			String meterTypeList = getMeterType(dictionaryValue.getDictionaryID().toString());
			JSONArray meterTypeListJson = JSONArray.fromObject(meterTypeList);
			meterTypeJson.put("meterTypeList", meterTypeListJson);
		}
		json.put("mt", meterTypeJson);
		
		return json.toString();
	}
	
	/**
	 * 模糊查询meter 根据分页限制，模糊查询所有字段
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getMeterByFuzzySearch",method=RequestMethod.POST) 
	@ResponseBody
	public String getMeterByFuzzySearch(HttpServletRequest req){
		String searchCondition = req.getParameter("searchCondition");
		String limitRows = req.getParameter("currentSelectPageSize");
		String currentPage = req.getParameter("currentSelectPage");
		String sortName = req.getParameter("sortName");
		String sortType = req.getParameter("sortType");
		logger.info(req.getRemoteAddr()+"请求查询表->Meter,模糊查询->"+searchCondition);
		
		int limit = 0;
		int current = 0;
		try{
			limit = Integer.parseInt(limitRows);
			current = Integer.parseInt(currentPage);
		}catch(NumberFormatException e){
			logger.error(e);
			logger.error("传入分页限制参数不为数字，limitRows->"+limitRows+"，currentPage->"+currentPage+"，非法参数!");
			JSONObject json = new JSONObject();
			JSONArray meterInfos = new JSONArray();
			json.put(Constant.JSON_ROWS, meterInfos);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}
		Map<String, Object> retMap = null;
		try{
			retMap = meterService.getMetersByFuzzy(searchCondition,limit,current,
					sortName,sortType);
		}catch(Exception e){
			logger.error(e);
			logger.info("select * from Meter where  模糊查询条件->"+searchCondition+"，返回空详细信息json数据体");
			JSONObject json = new JSONObject();
			json.put(Constant.JSON_COUNT, 0);
			JSONArray resultJsonArray = new JSONArray();
			json.put(Constant.JSON_ROWS, resultJsonArray);
			return json.toString();
		}
		JSONObject json = new JSONObject();
		json.put(Constant.JSON_COUNT, retMap.get("totalCount"));
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());
		List<Meter> meters = (List<Meter>) retMap.get("meters");
		JSONArray resultJsonArray = JSONArray.fromObject(meters,jsonConfig);
		json.put(Constant.JSON_ROWS, resultJsonArray);
		
		return json.toString();
	}
}
