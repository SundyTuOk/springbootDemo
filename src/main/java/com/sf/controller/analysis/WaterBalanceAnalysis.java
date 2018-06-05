package com.sf.controller.analysis;

import com.sf.service.WTestService;
import com.sf.utils.CommonUtils;
import com.sf.utils.Constant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 水平衡分析页面
 * @author 涂召亮
 *
 */
@Controller
@RequestMapping("/K202ttttttttt")
public class WaterBalanceAnalysis {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource(name = "wTestService")
	private WTestService wTestService;
	
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
		
		int totalCount = wTestService.getWaterBalanceAnalysisCount();
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
		
		
		List<Map<String, Object>> waterTestList = wTestService.getWaterBalanceAnalysis(start,end,
				sortName,sortType);
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
	
}
