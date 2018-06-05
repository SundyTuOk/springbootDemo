package com.sf.controller.K202;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.service.WTestService;
import com.sf.utils.CommonUtils;
import com.sf.utils.Constant;

@Controller
@RequestMapping("/K202")
public class K202 {
	
	private Logger logger = LoggerFactory.getLogger(K202.class);

	@Resource(name = "wTestService")
	private WTestService wTestService;
	
//	//1 获取区块的水平衡信息列表
//	@RequestMapping(value="/GetWTestList",method=RequestMethod.GET) 
//	@ResponseBody
//	public String GetWTestList(HttpServletRequest req) throws IOException{
//		/*	SELECT TOP 1000 [WTestParameter_ID]
//	      ,[Area_ID]
//	      ,[Name]
//	      ,[IsAuto]
//	      ,[Enable]
//	      ,[MonitoringTime]
//	      ,[BJ_leakage]
//	      ,[GY_leakage]
//	      ,[OrderIndex]
//	  FROM [WaterBalance].[dbo].[WTestParameter] order by OrderIndex */
////最多显示4个
//		String Area_ID = req.getParameter("Area_ID");
//		String sortName = req.getParameter("sortName");//
//		String sortType = req.getParameter("sortType");//
//		String SelectCon = req.getParameter("SelectCon");//
//
//	
//		//Type为空就当0处理
//		//ID,Num,Name
//		JSONArray jsonArray = new JSONArray();
//
//		JSONObject json1 = new JSONObject();
//		json1.put("WTestParameter_ID", "1");
//		json1.put("Area_ID", "1");
//		json1.put("Area_Name", "华中师范大学");//所属区块
//		json1.put("Name", "一级水平衡");
//		json1.put("IsAuto", "1");//是否为默认
//		json1.put("Enable", "1");//是否有效
//		json1.put("MonitoringTime", "02:00-04:00");//最小流量检查时间
//		json1.put("BJ_leakage", "10");//背景漏损量
//		json1.put("GY_leakage", "20");//漏损干预值
//		jsonArray.add(json1);
//
//		json1.put("WTestParameter_ID", "1");
//		json1.put("Area_ID", "1");
//		json1.put("Area_Name", "华中师范大学");//所属区块
//		json1.put("Name", "二级水平衡");
//		json1.put("IsAuto", "1");//是否为默认
//		json1.put("Enable", "1");//是否有效
//		json1.put("MonitoringTime", "02:00-04:00");//最小流量检查时间
//		json1.put("BJ_leakage", "10");//背景漏损量
//		json1.put("GY_leakage", "20");//漏损干预值
//		jsonArray.add(json1);
//		
//		JSONObject jsonR = new JSONObject();
//		jsonR.put("totalCount", 2);
//		jsonR.put("rows", jsonArray);
//		return jsonR.toString();
//	}
//	
	
	/**
	 * 水平衡分析列表显示
	 * @return
	 */
	@RequestMapping(value="/GetWTestList",method=RequestMethod.GET)
	@ResponseBody
	public String getWaterBalanceAnalysis(HttpServletRequest req){
		String sortName = req.getParameter("sortName");
		String sortType = req.getParameter("sortType");
		String currentPage = req.getParameter("currentSelectPage");
		String limitRows = req.getParameter("currentSelectPageSize");
		String selectCon = req.getParameter("SelectCon");
		String condition = null;
		if(selectCon != null){
			try {
				condition = new String(req.getParameter("SelectCon").getBytes("iso-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		int limit = 0;
		int current = 0;
		try{
			limit = Integer.parseInt(limitRows);
			current = Integer.parseInt(currentPage);
		}catch(NumberFormatException e){
			logger.error(e.toString());
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
		
		int start = ((current-1) * limit) + 1;
		int end = current * limit;
		
		int totalCount = 0;
		List<Map<String, Object>> waterTestList = null;
		if(condition == null){
			// 正常查询，不是模糊查询
			totalCount = wTestService.getWaterBalanceAnalysisCount();
			json.put(Constant.JSON_COUNT, totalCount);
			if(totalCount == 0){
				// 查询结果为空的数据
				JSONArray meterInfos = new JSONArray();
				json.put(Constant.JSON_ROWS, meterInfos);
				
				logger.debug("按照分页查询到meter数据量为0，返回json->"+json.toString());
				return json.toString();
			}
			waterTestList = wTestService.getWaterBalanceAnalysis(start,end,sortName,sortType);
		}else{
			// 模糊查询，要用condition条件
			totalCount = wTestService.getWaterBalanceAnalysisFuzzyCount(condition);
			json.put(Constant.JSON_COUNT, totalCount);
			if(totalCount == 0){
				// 查询结果为空的数据
				JSONArray meterInfos = new JSONArray();
				json.put(Constant.JSON_ROWS, meterInfos);
				
				logger.debug("按照分页查询到meter数据量为0，返回json->"+json.toString());
				return json.toString();
			}
			waterTestList = 
					wTestService.getWaterBalanceAnalysisFuzzy(start,end,sortName,sortType,condition);
		}
		
		JSONArray retJsonArray = new JSONArray();
		for (Map<String, Object> waterTestMap : waterTestList) {
			JSONObject waterTestjson = new JSONObject();
			waterTestjson.put("WTestParameter_ID", waterTestMap.get("WTestParameter_ID") == null ? "" : waterTestMap.get("WTestParameter_ID"));
			waterTestjson.put("Name", waterTestMap.get("Name") == null ? "" : waterTestMap.get("Name"));
			waterTestjson.put("Area_Name", waterTestMap.get("Area_Name") == null ? "" : waterTestMap.get("Area_Name"));
			waterTestjson.put("Enable", waterTestMap.get("Enable") == null ? "" : waterTestMap.get("Enable"));
			waterTestjson.put("IsAuto", waterTestMap.get("IsAuto") == null ? "" : waterTestMap.get("IsAuto"));
			waterTestjson.put("MonitoringTime", waterTestMap.get("MonitoringTime") == null ? "" : waterTestMap.get("MonitoringTime"));
			waterTestjson.put("BJ_leakage", waterTestMap.get("BJ_leakage") == null ? "" : waterTestMap.get("BJ_leakage"));
			waterTestjson.put("GY_leakage", waterTestMap.get("GY_leakage") == null ? "" : waterTestMap.get("GY_leakage"));
			retJsonArray.add(waterTestjson);
		}
		json.put(Constant.JSON_ROWS, retJsonArray);
		return json.toString();
	}
	
	
	/*//1 获取区块的水平衡信息列表
	@RequestMapping(value="/DelWTestList",method=RequestMethod.GET) 
	@ResponseBody
	public String DelWTestList(HttpServletRequest req) throws IOException{

//删除
		String WTestParameter_ID = req.getParameter("WTestParameter_ID");

		return CommonUtils.getFailJson().toString();
		//Area_ID,Name,IsAuto,Enable,MonitoringTime,BJ_leakage,GY_leakage,OrderIndex
	}
	//1 获取区块的水平衡信息列表
	@RequestMapping(value="/AddWTestList",method=RequestMethod.GET) 
	@ResponseBody
	public String AddWTestList(HttpServletRequest req) throws IOException{

//删除
		String Area_ID = req.getParameter("Area_ID");
		String Name = req.getParameter("Name");
		String IsAuto = req.getParameter("IsAuto");
		String Enable = req.getParameter("Enable");
		String MonitoringTime = req.getParameter("MonitoringTime");
		String BJ_leakage = req.getParameter("BJ_leakage");
		String GY_leakage = req.getParameter("GY_leakage");
		String OrderIndex = req.getParameter("OrderIndex");
		String InMeterList = req.getParameter("InMeterList");
		String OutMeterList = req.getParameter("OutMeterList");
		return CommonUtils.getFailJson().toString();
		//Area_ID,Name,IsAuto,Enable,MonitoringTime,BJ_leakage,GY_leakage,OrderIndex
		
		
	}*/
	
	/**
	 * 水平衡分析列表删除
	 * @return
	 */
	@RequestMapping(value="/DelWTestList",method=RequestMethod.GET)
	@ResponseBody
	public String deleteWaterBalanceAnalysis(HttpServletRequest req){
		String WTestParameter_ID = req.getParameter("WTestParameter_ID");
		int wTestParameterID = 0;
		try{
			wTestParameterID = Integer.parseInt(WTestParameter_ID);
		}catch(Exception e){
			logger.error(e.toString());
			logger.error("水平衡分析列表删除的请求参数异常WTestParameter_ID->"+WTestParameter_ID);
			return CommonUtils.getFailJson().toString();
		}
		
		boolean result = wTestService.deleteWTestParameterByWTestParameterID(wTestParameterID);
		if(result){
			return CommonUtils.getSuccessJson().toString();
		}
		return CommonUtils.getFailJson().toString();
	}
	
	/**
	 * 水平衡分析列表模糊查询的
	 * @return
	 */
	@RequestMapping(value="/searchWaterBalanceAnalysis",method=RequestMethod.GET)
	@ResponseBody
	public String searchWaterBalanceAnalysis(HttpServletRequest req){
		String sortName = req.getParameter("sortName");
		String sortType = req.getParameter("sortType");
		String currentPage = req.getParameter("currentSelectPage");
		String limitRows = req.getParameter("currentSelectPageSize");
		String condition = req.getParameter("condition");
		
		int limit = 0;
		int current = 0;
		try{
			limit = Integer.parseInt(limitRows);
			current = Integer.parseInt(currentPage);
		}catch(NumberFormatException e){
			logger.error(e.toString());
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
		
		int totalCount = wTestService.getWaterBalanceAnalysisFuzzyCount(condition);
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
		
		List<Map<String, Object>> waterTestList = 
				wTestService.getWaterBalanceAnalysisFuzzy(start,end,sortName,sortType,condition);
		JSONArray retJsonArray = new JSONArray();
		for (Map<String, Object> waterTestMap : waterTestList) {
			JSONObject waterTestjson = new JSONObject();
			waterTestjson.put("WTestParameter_ID", waterTestMap.get("WTestParameter_ID") == null ? "" : waterTestMap.get("WTestParameter_ID"));
			waterTestjson.put("Name", waterTestMap.get("Name") == null ? "" : waterTestMap.get("Name"));
			waterTestjson.put("Area_Name", waterTestMap.get("Area_Name") == null ? "" : waterTestMap.get("Area_Name"));
			waterTestjson.put("Enable", waterTestMap.get("Enable") == null ? "" : waterTestMap.get("Enable"));
			waterTestjson.put("IsAuto", waterTestMap.get("IsAuto") == null ? "" : waterTestMap.get("IsAuto"));
			waterTestjson.put("MonitoringTime", waterTestMap.get("MonitoringTime") == null ? "" : waterTestMap.get("MonitoringTime"));
			waterTestjson.put("BJ_leakage", waterTestMap.get("BJ_leakage") == null ? "" : waterTestMap.get("BJ_leakage"));
			waterTestjson.put("GY_leakage", waterTestMap.get("GY_leakage") == null ? "" : waterTestMap.get("GY_leakage"));
			retJsonArray.add(waterTestjson);
		}
		json.put(Constant.JSON_ROWS, retJsonArray);
		return json.toString();
	}
	
	//1 获取区块的水平衡信息列表
	@RequestMapping(value="/EditWTestList",method=RequestMethod.GET) 
	@ResponseBody
	public String EditWTestList(HttpServletRequest req) throws IOException{
		


		String WTestParameter_ID = req.getParameter("WTestParameter_ID");
		String Area_ID = req.getParameter("Area_ID");
		String Name = req.getParameter("Name");
		String IsAuto = req.getParameter("IsAuto");
		String Enable = req.getParameter("Enable");
		String MonitoringTime = req.getParameter("MonitoringTime");
		String BJ_leakage = req.getParameter("BJ_leakage");
		String GY_leakage = req.getParameter("GY_leakage");
		String OrderIndex = req.getParameter("OrderIndex");
		String InMeterList = req.getParameter("InMeterList");
		String OutMeterList = req.getParameter("OutMeterList");
		return CommonUtils.getFailJson().toString();
		//Area_ID,Name,IsAuto,Enable,MonitoringTime,BJ_leakage,GY_leakage,OrderIndex
	}
}
