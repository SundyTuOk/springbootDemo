package com.sf.controller.index;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.bean.Area;
import com.sf.bean.ErrList;
import com.sf.bean.IndexPageMeter;
import com.sf.bean.Meter;
import com.sf.bean.NetWork;
import com.sf.bean.NetWorkMeter;
import com.sf.bean.PNetWork;
import com.sf.bean.T_AreaDay;
import com.sf.bean.T_WADay;
import com.sf.controller.Gross.Gross;
import com.sf.dao.MeterDAO;
import com.sf.service.IndexService;
import com.sf.utils.CommonUtils;
import com.sf.utils.Constant;

@Controller
@RequestMapping("/index")
public class Index {
	
private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private MeterDAO meterDAO;

	@Autowired
	private IndexService indexService;
	@Autowired
	private Gross gross;
	
	//1
	@RequestMapping(value="/LoadTopArea",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadTopArea(HttpServletRequest req) throws IOException{
	
//		String Area_ID = req.getParameter("Area_ID");
//		
//		JSONArray jsonArray = new JSONArray();
//		JSONObject json1 = new JSONObject();
//		json1.put("Type", "Area");
//		json1.put("Name", "区块");
//		json1.put("Count", "20");
//		json1.put("Unit", "个");
//		jsonArray.add(json1);
//		json1.put("Type", "NetWork");
//		json1.put("Name", "管道");
//		json1.put("Count", "20");
//		json1.put("Unit", "个");
//		jsonArray.add(json1);
//		
//		json1.put("Type", "WA");
//		json1.put("Name", "水表");
//		json1.put("Count", "20");
//		json1.put("Unit", "个");
//		jsonArray.add(json1);
//	
//		json1.put("Type", "FL");
//		json1.put("Name", "流量计");
//		json1.put("Count", "20");
//		json1.put("Unit", "个");
//		jsonArray.add(json1);
//
//		json1.put("Type", "PR");
//		json1.put("Name", "压力计");
//		json1.put("Count", "20");
//		json1.put("Unit", "个");
//		jsonArray.add(json1);
//		
//		json1.put("Name", "阀门 ");
//		json1.put("Count", "20");
//		json1.put("Unit", "个");
//		jsonArray.add(json1);
//	/*		[
//				{
//			  "Type": "Area",
//			  "Name": "区块",
//			  "Count": "20",
//			   "Unit": "个"
//			},
//			{
//			  "Type": "NetWork",
//			  "Name": "管道",
//			  "Count": "20",
//			   "Unit": "个"
//			},
//			{
//			  "Type": "WA",
//			  "Name": "水表",
//			  "Count": "20",
//			   "Unit": "个"
//			},
//			{
//			  "Type": "FL",
//			  "Name": "流量计",
//			  "Count": "20",
//			   "Unit": "个"
//			},
//		]";
//*/		return jsonArray.toString();
		String Area_IDs = req.getParameter("Area_ID");
		Integer Area_ID = 0;
		
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		try {
			Area_ID = Integer.parseInt(Area_IDs);
		} catch (NumberFormatException e) {
			logger.error(e);
			logger.error("传入地区ID参数不为数字，Area_ID->" + Area_ID+"，非法参数!");
			json.put(Constant.JSON_ROWS, jsonArray);
			json.put(Constant.JSON_COUNT, 0);
			return json.toString();
		}
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		//地区数据
		Map<String, Object> areaMap = new HashMap<String, Object>();
		int AreaCount=indexService.getSubordinateRegion(Area_ID);
		areaMap.put("Name", "区块");
		areaMap.put("Type", "Area");
		areaMap.put("Count", AreaCount);
		areaMap.put("Unit", "个");
		resultList.add(areaMap);
		
		//管道数据
		Map<String, Object> netWorkMap = new HashMap<String, Object>();
		int NetWorkCount = indexService.getAreaNetWork(Area_ID);
		netWorkMap.put("Name", "管道");
		netWorkMap.put("Type", "NetWork");
		netWorkMap.put("Count", NetWorkCount);
		netWorkMap.put("Unit", "个");
		resultList.add(netWorkMap);
		
		//水表数据
		int waterMeterCount = indexService.
				getMeterCountByAreaIDAndMeterType(Area_ID, "WA");
		Map<String, Object> waterMeterMap = new HashMap<String, Object>();
		waterMeterMap.put("Name", "水表");
		waterMeterMap.put("Type", "WA");
		waterMeterMap.put("Count", waterMeterCount);
		waterMeterMap.put("Unit", "个");
		resultList.add(waterMeterMap);
		
		// 流量计数据
		int flMeterCount = indexService.
				getMeterCountByAreaIDAndMeterType(Area_ID, "FL");
		Map<String, Object> flMeterMap = new HashMap<String, Object>();
		flMeterMap.put("Name", "流量计");
		flMeterMap.put("Type", "FL");
		flMeterMap.put("Count", flMeterCount);
		flMeterMap.put("Unit", "个");
		resultList.add(flMeterMap);
		
		// 压力计数据
		int prMeterCount = indexService.
				getMeterCountByAreaIDAndMeterType(Area_ID, "PR");
		Map<String, Object> prMeterMap = new HashMap<String, Object>();
		prMeterMap.put("Name", "压力计");
		prMeterMap.put("Type", "PR");
		prMeterMap.put("Count", prMeterCount);
		prMeterMap.put("Unit", "个");
		resultList.add(prMeterMap);
		
		// 阀门数据
		int valveMeterCount = indexService.
				getMeterCountByAreaIDAndMeterType(Area_ID, "VALVE");
		Map<String, Object> valveMeterMap = new HashMap<String, Object>();
		valveMeterMap.put("Name", "阀门");
		valveMeterMap.put("Type", "VALVE");
		valveMeterMap.put("Count", valveMeterCount);
		valveMeterMap.put("Unit", "个");
		resultList.add(valveMeterMap);

		/*// 水箱数据
		int poolMeterCount = indexService.
				getMeterCountByAreaIDAndMeterType(Area_ID, "POOL");
		Map<String, Object> poolMeterMap = new HashMap<String, Object>();
		poolMeterMap.put("Name", "水箱");
		poolMeterMap.put("Type", "POOL");
		poolMeterMap.put("Count", poolMeterCount);
		poolMeterMap.put("Unit", "个");
		resultList.add(poolMeterMap);
		
		// 加压泵数据
		int pumpMeterCount = indexService.
				getMeterCountByAreaIDAndMeterType(Area_ID, "PUMP");
		Map<String, Object> pumpMeterMap = new HashMap<String, Object>();
		pumpMeterMap.put("Name", "加压泵");
		pumpMeterMap.put("Type", "PUMP");
		pumpMeterMap.put("Count", pumpMeterCount);
		pumpMeterMap.put("Unit", "个");
		resultList.add(pumpMeterMap);*/
		
		JSONArray netWorkInfos = JSONArray.fromObject(resultList);
		// 添加返回数据
		logger.debug("成功返回json->" + netWorkInfos.toString());
		return netWorkInfos.toString();
	}
	//4
	@RequestMapping(value="/LoadCharAreaGrossLose",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadCharAreaGrossLose(HttpServletRequest req) throws IOException{
		//获取区块的漏损信息
		String Area_ID = req.getParameter("Area_ID");
		String TheBeginDate = req.getParameter("TheBeginDate");//2018-05-01
		String TheEndDate = req.getParameter("TheEndDate");//2018-06-01

		JSONObject json1 = new JSONObject();
//		JSONObject jsonArrayMonthGross = NewWorkGross( Area_ID, TheBeginDate, TheEndDate);
//		
//		json1.put("MonthGross", jsonArrayMonthGross);
		int areaIDInt = 0;
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try{
			areaIDInt = Integer.parseInt(Area_ID);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			begin.setTime(sdf.parse(TheBeginDate));
			end.setTime(sdf.parse(TheEndDate));
		}catch(Exception e){
			logger.error(e);
			logger.error("请求参数异常Area_ID->"+Area_ID+",TheBeginDate->"+TheBeginDate+",TheEndDate->"+TheEndDate);
			return CommonUtils.getFailJson().toString();
		}
		List<Integer> meterIDList = new ArrayList<Integer>();
		meterIDList.add(areaIDInt);
		List<T_AreaDay> t_WADayList = indexService.getT_AreaDayBetween(meterIDList, TheBeginDate, TheEndDate);
		List<Float> inList = new ArrayList<Float>();// 日进水
		List<Float> outList = new ArrayList<Float>();// 日出水
		List<Float> loseList = new ArrayList<Float>();
		List<Float> loseRateList = new ArrayList<Float>();
		float totalIn = 0F;// 总进水量
		float totalOut = 0F;// 总出水量
		StringBuffer indexStringBuffer = new StringBuffer();
		StringBuffer iteratorStringBuffer = new StringBuffer();
		boolean hasData = false;
		Iterator<T_AreaDay> iterator = null;
		while(!begin.after(end)){
			float in = 0F;
			float out = 0F;
			float lose = 0F;
			float loseRate = 0F;
			indexStringBuffer.setLength(0);
			indexStringBuffer.append(begin.get(Calendar.YEAR)).append("-")
			.append(begin.get(Calendar.MONTH)+1).append("-")
			.append(begin.get(Calendar.DAY_OF_MONTH));
			hasData = false;
			iterator = t_WADayList.iterator();
			
			while(iterator.hasNext()){
				T_AreaDay t_AreaDay = iterator.next();
				iteratorStringBuffer.setLength(0);
				iteratorStringBuffer.append(t_AreaDay.getSelectYear()).append("-")
				.append(t_AreaDay.getSelectMonth()).append("-")
				.append(t_AreaDay.getSelectDay());
				
				if(indexStringBuffer.toString().equals(iteratorStringBuffer.toString())){
					hasData = true;// 当天有数据标志
					in = t_AreaDay.getzGrossIn() == null ? 0F : t_AreaDay.getzGrossIn();// 记录当天日进水量
					out = t_AreaDay.getzGrossOut() == null ? 0F : t_AreaDay.getzGrossOut();// 记录当天日出水量
				}
				
			}
			// 当前没有数据，就给当天添加一条0记录
			if(!hasData){
				in = 0F;
				out = 0F;
			}
			lose = CommonUtils.subtract(in, out);// 当天漏损量
			// 计算当天漏损率
			if(in == 0F){
				loseRate = 0F;
			}else{
				loseRate = CommonUtils.divide(lose, in, 2, BigDecimal.ROUND_UP);
			}
			
			inList.add(in);// 进水量
			outList.add(out);// 出水量
			loseList.add(lose);// 漏损
			loseRateList.add(loseRate);// 漏损率
			totalIn = CommonUtils.add(in, totalIn);// 总进水
			totalOut = CommonUtils.add(out, totalOut);// 总出水
			begin.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		float totalLose = CommonUtils.subtract(totalIn, totalOut);// 总漏失
		// 今年这个时间段总漏失率
		float countLoseRa = 0F;
		if(totalIn == 0F){
			// 被除数为0 无法计算总漏损率
			countLoseRa = 0F;
		}else{
			countLoseRa = CommonUtils.divide(totalLose, totalIn, 2, BigDecimal.ROUND_UP);
		}
		
		JSONObject jsonGross = new JSONObject();
		Float[] stringArrIn = new Float[inList.size()];
		inList.toArray(stringArrIn);
		jsonGross.put("In", stringArrIn);
		
		Float[] stringArrOut = new Float[outList.size()];
		outList.toArray(stringArrOut);
		jsonGross.put("Out", stringArrOut);
		
		Float[] stringArrLose = new Float[loseList.size()];
		loseList.toArray(stringArrLose);
		jsonGross.put("Lose", stringArrLose);
		
		Float[] stringArrLoseRate = new Float[loseRateList.size()];
		loseRateList.toArray(stringArrLoseRate);
		jsonGross.put("Loserate", stringArrLoseRate);
		
		json1.put("MonthGross", jsonGross);
		
		// 去年这个时间段总漏失率
		begin.add(Calendar.YEAR, -1);
		end.add(Calendar.YEAR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastYearBeginDateStr = sdf.format(begin.getTime());
		String lastYearendDateStr = sdf.format(end.getTime());
		List<T_AreaDay> lastYearT_WADayList = indexService.getT_AreaDayBetween(meterIDList, lastYearBeginDateStr, lastYearendDateStr);
		float lastYearTotalIn = 0F;// 去年同期总进水
		float lastYearTotalOut = 0F;// 去年同期总出水
		for (T_AreaDay t_AreaDay : lastYearT_WADayList) {
			float tempIn = (t_AreaDay.getzGrossIn() == null ? 0F : t_AreaDay.getzGrossIn());
			float tempOut = (t_AreaDay.getzGrossOut() == null ? 0F : t_AreaDay.getzGrossOut());
			lastYearTotalIn = CommonUtils.add(tempIn, lastYearTotalIn);
			lastYearTotalOut = CommonUtils.add(tempOut, lastYearTotalOut);
		}
		// 去年同期漏损率
		float loseRateLastYearMonth = 0F;
		if(lastYearTotalIn == 0){
			loseRateLastYearMonth = 0F;
		}else{
			// 去年同期漏损
			float loseLastYearMonth = CommonUtils.subtract(lastYearTotalIn, lastYearTotalOut);
			loseRateLastYearMonth = CommonUtils.divide(loseLastYearMonth, lastYearTotalIn, 4, BigDecimal.ROUND_UP);
		}
		
		// 漏损率年同比，今天同期漏损率/去年同期漏损率
		float countLoseRaBT = 0F;
		if(loseRateLastYearMonth == 0F){
			countLoseRaBT = 0F;
		}else{
			countLoseRaBT = CommonUtils.divide(countLoseRa, loseRateLastYearMonth, 4, BigDecimal.ROUND_UP);
		}
		
		
		json1.put("CountInGross", totalIn);//总进水
		json1.put("CountOutGross", totalOut);//总出水
		json1.put("CountLoseGross", totalLose);//总漏失
		json1.put("CountLoseRa", countLoseRa);//总漏损率
		json1.put("CountLoseRaBT", countLoseRaBT);//总漏损率年同比
//		json1.put("CountInGross", 1200);//总进水
//		json1.put("CountOutGross", 800);//总出水
//		json1.put("CountLoseGross", 800);//总漏失
//		json1.put("CountLoseRa", 1200);//总漏损率
//		json1.put("CountLoseRaBT", 10);//总漏损率年同比
		
		return json1.toString();
	}
	
	//3
	@RequestMapping(value="/LoadTopAreaInfo",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadTopAreaInfo(HttpServletRequest req) throws IOException{
	
		String Area_ID = req.getParameter("Area_ID");
		String Type = req.getParameter("Type");//NetWork,WA....
		String ID = req.getParameter("ID");//1
		String TheBeginDate = req.getParameter("TheBeginDate");//2018-05-01
		String TheEndDate = req.getParameter("TheEndDate");//2018-06-01
		//ID,Num,Name
		// 检查参数
		if(Area_ID == null || Type == null || 
				ID == null || TheBeginDate == null || 
				TheEndDate == null || Area_ID.isEmpty() || 
				Type.isEmpty() || ID.isEmpty() || 
				TheBeginDate.isEmpty() || TheEndDate.isEmpty()){
			logger.error("LoadTopAreaInfo参数异常");
			return CommonUtils.getFailJson().toString();
		}
		int IDInt = 0;
		try{
			IDInt = Integer.parseInt(ID);
		}catch(Exception e){
			logger.error("参数异常ID->"+ID);
			return CommonUtils.getFailJson().toString();
		}
		// 要返回的json
		JSONObject json1 = new JSONObject();
		// 接口分支,根据不同的type，查询不同的值
		if(Type.equals("NetWork") )
		{
			//NetWork_ID,NetWork_Num,NetWork_Name,NetWork_Caliber,NetWork_TotalLenght,PipeNetworkType
//			json1.put("NetWork_ID", "1");
//			json1.put("NetWork_Num", "0016");
//			json1.put("NetWork_Name", "国交泵房总出水");
//			json1.put("NetWork_Caliber", "200");
//			json1.put("NetWork_TotalLenght", "1.9");
//			json1.put("PipeNetworkType", "主干网");
			NetWork netWork = indexService.getNetWorkByNetWorkID(IDInt);
			if(netWork == null){
				logger.info("没有ID为"+ID+"的NetWork,接口返回失败json");
				return CommonUtils.getFailJson().toString();
			}
			json1.put("NetWork_ID", netWork.getNetworkID() == null ? "" : netWork.getNetworkID());
			json1.put("NetWork_Num", netWork.getNetworkNum() == null ? "" : netWork.getNetworkNum());
			json1.put("NetWork_Name", netWork.getNetworkName() == null ? "" : netWork.getNetworkName());
			json1.put("NetWork_Caliber", netWork.getNetworkCaliber() == null ? "" : netWork.getNetworkCaliber());
			json1.put("NetWork_TotalLenght", netWork.getNetworkTotalLenght() == null ? "" : netWork.getNetworkTotalLenght());
			json1.put("PipeNetworkType", netWork.getPipeNetworkType() == null ? "" : netWork.getPipeNetworkType());
			//时间段内月用水图，包括入水、出水、漏损率信息
			JSONObject jsonArrayMonthGross = NewWorkGross( ID, TheBeginDate, TheEndDate);
			json1.put("MonthGross", jsonArrayMonthGross);
		}
		else if(Type.equals("WA") )//水表
		{
			//select Meter_ID,Meter_Num,Meter_Name,Meter_485Address,Beilv,Xiuzheng,Meter_Changjia,Meter_Time,Meter_Value1,Meter_Value2,Meter_Value3,Meter_Value4,Meter_Remark from Meter 
//			json1.put("Meter_ID", "1");
//			json1.put("Meter_Num", "0016");
//			json1.put("Meter_Name", "国交泵房总出水");
//			json1.put("Meter_485Address", "002156545");
//			json1.put("Beilv", "1");
//			json1.put("Xiuzheng", "0");
//			json1.put("Meter_Changjia", "盛帆电子");
//			json1.put("Meter_Time", "2018-05-10 15:30:00");
//			json1.put("Meter_Value1", "157");
//			json1.put("Meter_Value2", "157");
//			json1.put("Meter_Value3", "157");
//			json1.put("Meter_Value4", "157");
//			json1.put("Meter_Remark", "");
			Meter meter = indexService.getMeterByMeterID(IDInt);
			json1.put("Meter_ID", meter.getMeterID() == null ? "" :meter.getMeterID());
			json1.put("Meter_Num", meter.getMeterNum() == null ? "" : meter.getMeterNum());
			json1.put("Meter_Name", meter.getMeterName() == null ? "" : meter.getMeterName());
			json1.put("Meter_485Address", meter.getMeter485Address() == null ? "" : meter.getMeter485Address());
			json1.put("Beilv", meter.getBeilv() == null ? "" : meter.getBeilv());
			json1.put("Xiuzheng", meter.getXiuzheng() == null ? "" : meter.getXiuzheng());
			json1.put("Meter_Changjia", meter.getMeterChangjia() == null ? "" : meter.getMeterChangjia());
			
			if(meter.getMeterTime() != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
				json1.put("Meter_Time", sdf.format(meter.getMeterTime()));
				sdf = null;
			}else{
				json1.put("Meter_Time", "");
			}
			
			json1.put("Meter_Value1", meter.getMeterValue1() == null ? "" : meter.getMeterValue1());
			json1.put("Meter_Value2", meter.getMeterValue2() == null ? "" : meter.getMeterValue2());
			json1.put("Meter_Value3", meter.getMeterValue3() == null ? "" : meter.getMeterValue3());
			json1.put("Meter_Value4", meter.getMeterValue4() == null ? "" : meter.getMeterValue4());
			json1.put("Meter_Remark", meter.getMeterRemark() == null ? "" : meter.getMeterRemark());
			//特性集合
			JSONArray jsonArrayMeterTexing = new JSONArray();
			JSONObject jsonTX = new JSONObject();
			jsonTX.put("MeterTexing_Name", "口径");
			jsonTX.put("TexingValue", "DN20");
			jsonTX.put("TexingText", "DN20");
			jsonArrayMeterTexing.add(jsonTX);

			json1.put("TexingList", jsonArrayMeterTexing);
			
			//时间段内月用水图
			JSONObject jsonArrayMonthGross =WAGross( ID, TheBeginDate, TheEndDate);
			json1.put("MonthGross", jsonArrayMonthGross);
		}
		else if(Type.equals("FL") )//流量计
		{
			//select Meter_ID,Meter_Num,Meter_Name,Meter_485Address,Beilv,Xiuzheng,Meter_Changjia,Meter_Time,Meter_Value1,Meter_Value2,Meter_Value3,Meter_Value4,Meter_Remark from Meter 
			
//			json1.put("Meter_ID", "1");
//			json1.put("Meter_Num", "0016");
//			json1.put("Meter_Name", "国交泵房总出水");
//			json1.put("Meter_485Address", "002156545");
//			json1.put("Beilv", "1");
//			json1.put("Xiuzheng", "0");
//			json1.put("Meter_Changjia", "盛帆电子");
//			json1.put("Meter_Time", "2018-05-10 15:30:00");
//			json1.put("Meter_Value1", "157");
//			json1.put("Meter_Value2", "157");
//			json1.put("Meter_Value3", "157");
//			json1.put("Meter_Value4", "157");
//			json1.put("Meter_Remark", "");
			//特性集合
//			JSONArray jsonArrayMeterTexing = new JSONArray();
//			JSONObject jsonTX = new JSONObject();
//			jsonTX.put("MeterTexing_Name", "口径");
//			jsonTX.put("TexingValue", "DN20");
//			jsonTX.put("TexingText", "DN20");
//			jsonArrayMeterTexing.add(jsonTX);
			
			Meter meterFL = indexService.getMeterByMeterID(IDInt);
			if(meterFL == null){
				logger.info("没有ID为"+ID+"的FL，接口返回失败json");
				return CommonUtils.getFailJson().toString();
			}
			json1.put("Meter_ID", meterFL.getMeterID() == null ? "" : meterFL.getMeterID());
			json1.put("Meter_Num", meterFL.getMeterNum() == null ? "" : meterFL.getMeterNum());
			json1.put("Meter_Name", meterFL.getMeterName() == null ? "" : meterFL.getMeterName());
			json1.put("Meter_485Address", meterFL.getMeter485Address() == null ? "" : meterFL.getMeter485Address());
			json1.put("Beilv", meterFL.getBeilv() == null ? "" : meterFL.getBeilv());
			json1.put("Xiuzheng", meterFL.getXiuzheng() == null ? "" : meterFL.getXiuzheng());
			json1.put("Meter_Changjia", meterFL.getMeterChangjia() == null ? "" : meterFL.getMeterChangjia());
			if(meterFL.getMeterTime() == null){
				json1.put("Meter_Time", "");
			}else{
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
					String meterTimeString = sdf.format(meterFL.getMeterTime());
					json1.put("Meter_Time", meterTimeString);
				}catch(Exception e){
					json1.put("Meter_Time", "");
				}
			}
			json1.put("Meter_Value1", meterFL.getMeterValue1() == null ? "" : meterFL.getMeterValue1());
			json1.put("Meter_Value2", meterFL.getMeterValue2() == null ? "" : meterFL.getMeterValue2());
			json1.put("Meter_Value3", meterFL.getMeterValue3() == null ? "" : meterFL.getMeterValue3());
			json1.put("Meter_Value4", meterFL.getMeterValue4() == null ? "" : meterFL.getMeterValue4());
			json1.put("Meter_Remark", meterFL.getMeterRemark() == null ? "" : meterFL.getMeterRemark());
			
			//特性集合
			JSONArray jsonArrayMeterTexing = new JSONArray();
			List<Map<String, Object>> meterTexingList = indexService.getMeterTexingByMeterID(IDInt);
			for (Map<String, Object> map : meterTexingList) {
				JSONObject jsonTX = new JSONObject();
				jsonTX.put("MeterTexing_Name", map.get("MeterTexing_Name"));
				jsonTX.put("TexingValue", map.get("TexingValue"));
				jsonTX.put("TexingText", map.get("TexingValue"));
				jsonArrayMeterTexing.add(jsonTX);
			}
			json1.put("TexingList", jsonArrayMeterTexing);
			
			//时间段内瞬时流量
			JSONArray jsonArray =FLFlow ( ID, TheBeginDate, TheEndDate);
			json1.put("MonthFlow", jsonArray);
		}
		else if(Type.equals("PR") )//压力计
		{
			//select Meter_ID,Meter_Num,Meter_Name,Meter_485Address,Beilv,Xiuzheng,Meter_Changjia,Meter_Time,Meter_Value1,Meter_Value2,Meter_Value3,Meter_Value4,Meter_Remark from Meter 
//			json1.put("Meter_ID", "1");
//			json1.put("Meter_Num", "0016");
//			json1.put("Meter_Name", "国交泵房总出水");
//			json1.put("Meter_485Address", "002156545");
//			json1.put("Beilv", "1");
//			json1.put("Xiuzheng", "0");
//			json1.put("Meter_Changjia", "盛帆电子");
//			json1.put("Meter_Time", "2018-05-10 15:30:00");
//			json1.put("Meter_Value1", "157");
//			json1.put("Meter_Value2", "157");
//			json1.put("Meter_Value3", "157");
//			json1.put("Meter_Value4", "157");
//			json1.put("Meter_Remark", "");
//			//特性集合
//			JSONArray jsonArrayMeterTexing = new JSONArray();
//			JSONObject jsonTX = new JSONObject();
//			jsonTX.put("MeterTexing_Name", "口径");
//			jsonTX.put("TexingValue", "DN20");
//			jsonTX.put("TexingText", "DN20");
//			jsonArrayMeterTexing.add(jsonTX);
//
//			json1.put("TexingList", jsonArrayMeterTexing);
			
			Meter meterPR = indexService.getMeterByMeterID(IDInt);
			if(meterPR == null){
				logger.info("没有ID为"+ID+"的PR，接口返回失败json");
				return CommonUtils.getFailJson().toString();
			}
			json1.put("Meter_ID", meterPR.getMeterID() == null ? "" : meterPR.getMeterID());
			json1.put("Meter_Num", meterPR.getMeterNum() == null ? "" : meterPR.getMeterNum());
			json1.put("Meter_Name", meterPR.getMeterName() == null ? "" : meterPR.getMeterName());
			json1.put("Meter_485Address", meterPR.getMeter485Address() == null ? "" : meterPR.getMeter485Address());
			json1.put("Beilv", meterPR.getBeilv() == null ? "" : meterPR.getBeilv());
			json1.put("Xiuzheng", meterPR.getXiuzheng() == null ? "" : meterPR.getXiuzheng());
			json1.put("Meter_Changjia", meterPR.getMeterChangjia() == null ? "" : meterPR.getMeterChangjia());
			if(meterPR.getMeterTime() == null){
				json1.put("Meter_Time", "");
			}else{
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
					String meterTimeString = sdf.format(meterPR.getMeterTime());
					json1.put("Meter_Time", meterTimeString);
				}catch(Exception e){
					json1.put("Meter_Time", "");
				}
			}
			json1.put("Meter_Value1", meterPR.getMeterValue1() == null ? "" : meterPR.getMeterValue1());
			json1.put("Meter_Value2", meterPR.getMeterValue2() == null ? "" : meterPR.getMeterValue2());
			json1.put("Meter_Value3", meterPR.getMeterValue3() == null ? "" : meterPR.getMeterValue3());
			json1.put("Meter_Value4", meterPR.getMeterValue4() == null ? "" : meterPR.getMeterValue4());
			json1.put("Meter_Remark", meterPR.getMeterRemark() == null ? "" : meterPR.getMeterRemark());
			
			//特性集合
			JSONArray jsonArrayMeterTexing = new JSONArray();
			List<Map<String, Object>> meterTexingList = indexService.getMeterTexingByMeterID(IDInt);
			for (Map<String, Object> map : meterTexingList) {
				JSONObject jsonTX = new JSONObject();
				jsonTX.put("MeterTexing_Name", map.get("MeterTexing_Name"));
				jsonTX.put("TexingValue", map.get("TexingValue"));
				jsonTX.put("TexingText", map.get("TexingValue"));
				jsonArrayMeterTexing.add(jsonTX);
			}
			json1.put("TexingList", jsonArrayMeterTexing);
			
			//时间段内瞬时压力
			JSONArray jsonArray =PRPressure ( ID, TheBeginDate, TheEndDate);
			json1.put("MonthPressure", jsonArray);
		}
		else if(Type.equals("VALVE") )//阀门
		{
			//select Meter_ID,Meter_Num,Meter_Name,Meter_485Address,Beilv,Xiuzheng,Meter_Changjia,Meter_Time,Meter_Value1,Meter_Value2,Meter_Value3,Meter_Value4,Meter_Remark from Meter 
//			json1.put("Meter_ID", "1");
//			json1.put("Meter_Num", "0016");
//			json1.put("Meter_Name", "国交泵房总出水");
//			json1.put("Meter_485Address", "002156545");
//			json1.put("Beilv", "1");
//			json1.put("Xiuzheng", "0");
//			json1.put("Meter_Changjia", "盛帆电子");
//			json1.put("Meter_Time", "2018-05-10 15:30:00");
//			json1.put("Meter_Value1", "157");
//			json1.put("Meter_Value2", "157");
//			json1.put("Meter_Value3", "157");
//			json1.put("Meter_Value4", "157");
//			json1.put("Meter_Remark", "");
//			//特性集合
//			JSONArray jsonArrayMeterTexing = new JSONArray();
//			JSONObject jsonTX = new JSONObject();
//			jsonTX.put("MeterTexing_Name", "口径");
//			jsonTX.put("TexingValue", "DN20");
//			jsonTX.put("TexingText", "DN20");
//			jsonArrayMeterTexing.add(jsonTX);
//
//			json1.put("TexingList", jsonArrayMeterTexing);
			
			Meter meterPR = indexService.getMeterByMeterID(IDInt);
			if(meterPR == null){
				logger.info("没有ID为"+ID+"的PR，接口返回失败json");
				return CommonUtils.getFailJson().toString();
			}
			json1.put("Meter_ID", meterPR.getMeterID() == null ? "" : meterPR.getMeterID());
			json1.put("Meter_Num", meterPR.getMeterNum() == null ? "" : meterPR.getMeterNum());
			json1.put("Meter_Name", meterPR.getMeterName() == null ? "" : meterPR.getMeterName());
			json1.put("Meter_485Address", meterPR.getMeter485Address() == null ? "" : meterPR.getMeter485Address());
			json1.put("Beilv", meterPR.getBeilv() == null ? "" : meterPR.getBeilv());
			json1.put("Xiuzheng", meterPR.getXiuzheng() == null ? "" : meterPR.getXiuzheng());
			json1.put("Meter_Changjia", meterPR.getMeterChangjia() == null ? "" : meterPR.getMeterChangjia());
			if(meterPR.getMeterTime() == null){
				json1.put("Meter_Time", "");
			}else{
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
					String meterTimeString = sdf.format(meterPR.getMeterTime());
					json1.put("Meter_Time", meterTimeString);
				}catch(Exception e){
					json1.put("Meter_Time", "");
				}
			}
			json1.put("Meter_Value1", meterPR.getMeterValue1() == null ? "" : meterPR.getMeterValue1());
			json1.put("Meter_Value2", meterPR.getMeterValue2() == null ? "" : meterPR.getMeterValue2());
			json1.put("Meter_Value3", meterPR.getMeterValue3() == null ? "" : meterPR.getMeterValue3());
			json1.put("Meter_Value4", meterPR.getMeterValue4() == null ? "" : meterPR.getMeterValue4());
			json1.put("Meter_Remark", meterPR.getMeterRemark() == null ? "" : meterPR.getMeterRemark());
			
			//特性集合
			JSONArray jsonArrayMeterTexing = new JSONArray();
			List<Map<String, Object>> meterTexingList = indexService.getMeterTexingByMeterID(IDInt);
			for (Map<String, Object> map : meterTexingList) {
				JSONObject jsonTX = new JSONObject();
				jsonTX.put("MeterTexing_Name", map.get("MeterTexing_Name"));
				jsonTX.put("TexingValue", map.get("TexingValue"));
				jsonTX.put("TexingText", map.get("TexingValue"));
				jsonArrayMeterTexing.add(jsonTX);
			}
			json1.put("TexingList", jsonArrayMeterTexing);
		
		}
		//
		return json1.toString();
	}

	//5
	private	JSONObject NewWorkGross(String NetWork_ID,String TheBeginDate,String TheEndDate)
	{
//		JSONObject jsonArrayMonthGross = new JSONObject();
//		//起始时间至终止时间内的进水量（如果日期内没有用量，就插入""请不要插入0,个数要对）
//		String[] jsonArrayIn = {"230","220","241","241","241","241","241","241","241","241","230","220","241","200","245","244","220","302","304","300","","","","","","","","","",""};
//		//出水
//		String[] jsonArrayOut = {"230","220","241","241","241","241","241","241","241","241","230","220","241","200","245","244","220","302","304","300","","","","","","","","","",""};
//		//漏失量
//		String[] jsonArrayLose = {"230","220","241","241","241","241","241","241","241","241","230","220","241","200","245","244","220","302","304","300","","","","","","","","","",""};
//		//漏损率
//		String[] jsonArrayLoserate = {"230","220","241","241","241","241","241","241","241","241","230","220","241","200","245","244","220","302","304","300","","","","","","","","","",""};
//		jsonArrayMonthGross.put("In", jsonArrayIn);
//		jsonArrayMonthGross.put("Out", jsonArrayOut);
//		jsonArrayMonthGross.put("Lose", jsonArrayLose);
//		jsonArrayMonthGross.put("Loserate", jsonArrayLoserate);
//		return jsonArrayMonthGross;
		JSONObject json = new JSONObject();
		// 解析开始 结束时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = sdf.parse(TheBeginDate);
			endDate = sdf.parse(TheEndDate);
		} catch (ParseException e) {
			json.put("result", "error");
			return json;
		}
		begin.setTime(beginDate);
		end.setTime(endDate);
		
		int netWorkIDInt = 0;
		try{
			netWorkIDInt = Integer.parseInt(NetWork_ID);
		}catch(Exception e){
			json.put("result", "error");
			logger.error("参数异常:NetWork_ID->"+NetWork_ID);
			return json;
		}
		
		// 查询本管网下所有入水表ID 已及所有本管网下所有出水表ID
		List<NetWorkMeter> netWorkMeterList = indexService.
				getNetWorkMeterByNetWorkID(netWorkIDInt);
		List<Integer> inMeterIDList = new ArrayList<Integer>();
		List<Integer> outMeterIDList = new ArrayList<Integer>();
		for(NetWorkMeter netWorkMeter : netWorkMeterList){
			if(!netWorkMeter.getMeterType().equals("WA")){
				// 不是水表，忽略
				continue;
			}
			// 是水表 DictionaryValue里面查出 1 入水表 2 中间表 3 出水表
			if(netWorkMeter.getMeterStyle().equals("1") 
					&& netWorkMeter.getMeterID() != null){
				inMeterIDList.add(netWorkMeter.getMeterID());
				continue;
			}
			if(netWorkMeter.getMeterStyle().equals("3") 
					&& netWorkMeter.getMeterID() != null){
				outMeterIDList.add(netWorkMeter.getMeterID());
				continue;
			}
		}
		// 进水数据
		List<T_WADay> inT_WADayList = null;
		List<T_WADay> outT_WADayList = null;
		if(inMeterIDList.size() == 0){
			inT_WADayList = new ArrayList<T_WADay>();
		}else{
			inT_WADayList = indexService.getT_WADayBetween(inMeterIDList, TheBeginDate, TheEndDate);
		}
		// 出水数据
		if(outMeterIDList.size() == 0){
			outT_WADayList = new ArrayList<T_WADay>();
		}else{
			outT_WADayList = indexService.getT_WADayBetween(outMeterIDList, TheBeginDate, TheEndDate);
		}
		
		List<String> inList = new ArrayList<String>();
		List<String> outList = new ArrayList<String>();
		List<String> loseList = new ArrayList<String>();
		List<String> loseRateList = new ArrayList<String>();
		float totalIn = 0F;// 总进水量
		float totalOut = 0F;// 总出水量
		StringBuffer indexStringBuffer = new StringBuffer();
		StringBuffer inIteratorStringBuffer = new StringBuffer();
		StringBuffer outIteratorStringBuffer = new StringBuffer();
		boolean inHasData = false;
		boolean outHasData = false;
		Iterator<T_WADay> inIterator = null;
		Iterator<T_WADay> outIterator = null;
		while(!begin.after(end)){
			float in = 0F;
			float out = 0F;
			float lose = 0F;
			float loseRate = 0F;
			indexStringBuffer.setLength(0);
			indexStringBuffer.append(begin.get(Calendar.YEAR)).append("-")
			.append(begin.get(Calendar.MONTH)+1).append("-")
			.append(begin.get(Calendar.DAY_OF_MONTH));
			inHasData = false;
			outHasData = false;
			
//			for(Integer meterID : inMeterIDList){
				// 入水
//				T_WADay t_WADay = indexService.
//					getT_WADayBygetT_WADayByMeterIDAndDate(meterID, 
//							begin.get(Calendar.YEAR), 
//							begin.get(Calendar.MONTH),
//							begin.get(Calendar.DAY_OF_MONTH));
//				if(t_WADay == null || t_WADay.getzGross() == null){
//					continue;
//				}
//				in = CommonUtils.add(in, t_WADay.getzGross() == null ? 0F : t_WADay.getzGross());
//				totalIn = CommonUtils.add(in, totalIn);
//			}
			
//			for(Integer meterID : outMeterIDList){
				// 出水
//				T_WADay t_WADay = indexService.
//					getT_WADayBygetT_WADayByMeterIDAndDate(meterID, 
//							begin.get(Calendar.YEAR), 
//							begin.get(Calendar.MONTH),
//							begin.get(Calendar.DAY_OF_MONTH));
//				if(t_WADay == null || t_WADay.getzGross() == null){
//					continue;
//				}
//				out = CommonUtils.add(out, t_WADay.getzGross() == null ? 0F : t_WADay.getzGross());
//				totalOut = CommonUtils.add(out, totalOut);
//			}
			inIterator = inT_WADayList.iterator();
			outIterator = outT_WADayList.iterator();
			// 进水
			while(inIterator.hasNext()){
				T_WADay t_WADay = inIterator.next();
				inIteratorStringBuffer.setLength(0);
				inIteratorStringBuffer.append(t_WADay.getSelectYear()).append("-")
				.append(t_WADay.getSelectMonth()).append("-")
				.append(t_WADay.getSelectDay());
				// 关键点 *************   这里主要是要找到这天的数据
				if(indexStringBuffer.toString().equals(inIteratorStringBuffer.toString())){
					inList.add(t_WADay.getzGross() == null ? "0" : t_WADay.getzGross().toString());
					inHasData = true;
					inIterator.remove();
				}
			}
			if(!inHasData){
				inList.add("0");
			}
			// 出水
			while(outIterator.hasNext()){
				T_WADay t_WADay = outIterator.next();
				outIteratorStringBuffer.setLength(0);
				outIteratorStringBuffer.append(t_WADay.getSelectYear()).append("-")
				.append(t_WADay.getSelectMonth()).append("-")
				.append(t_WADay.getSelectDay());
				// 关键点 *************   这里主要是要找到这天的数据
				if(indexStringBuffer.toString().equals(outIteratorStringBuffer.toString())){
					outList.add(t_WADay.getzGross() == null ? "0" : t_WADay.getzGross().toString());
					outHasData = true;
					outIterator.remove();
				}
			}
			if(!outHasData){
				outList.add("0");
			}
			
//			inList.add(in + "");
//			outList.add(out + "");
			lose = CommonUtils.subtract(in, out);
			loseList.add(lose + "");
			// 保留2位小数 四舍五入,判断被除数是否为0
			loseRate = (in == 0F ? 0F :CommonUtils.divide(lose, in, 2, BigDecimal.ROUND_UP));
			loseRateList.add(loseRate + "");
			//加一天
			begin.add(Calendar.DAY_OF_MONTH, 1);
		}
		json.put("In", inList.toArray());
		json.put("Out", outList.toArray());
		json.put("Lose", loseList.toArray());
		json.put("Loserate", loseRateList.toArray());
		
		// 为方便后面，这里添加三个字段: 总进水、总出水、去年这个月的总漏失
		json.put("totalIn", totalIn);
		json.put("totalOut", totalOut);
		
		// 下面开始计算去年这个时间段内 管网的总进水和总出水
		begin.setTime(beginDate);
		end.setTime(endDate);
		// 时间设置为去年
		begin.add(Calendar.YEAR, -1);
		end.add(Calendar.YEAR, -1);
		float totalInLastYearMonth = 0F;// 去年这个时间段总进水量
		float totalOutLastYearMonth = 0F;// 去年这个时间段总出水量
//		while(!begin.after(end)){
//			for(Integer meterID : inMeterIDList){
//				// 入水
//				T_WADay t_WADay = indexService.
//					getT_WADayBygetT_WADayByMeterIDAndDate(meterID, 
//							begin.get(Calendar.YEAR), 
//							begin.get(Calendar.MONTH),
//							begin.get(Calendar.DAY_OF_MONTH));
//				if(t_WADay.getzGross() == null){
//					continue;
//				}
//				totalInLastYearMonth = CommonUtils.add(totalInLastYearMonth, t_WADay.getzGross());
//			}
//			
//			for(Integer meterID : outMeterIDList){
//				// 出水
//				T_WADay t_WADay = indexService.
//					getT_WADayBygetT_WADayByMeterIDAndDate(meterID, 
//							begin.get(Calendar.YEAR), 
//							begin.get(Calendar.MONTH),
//							begin.get(Calendar.DAY_OF_MONTH));
//				if(t_WADay.getzGross() == null){
//					continue;
//				}
//				totalOutLastYearMonth = CommonUtils.add(totalOutLastYearMonth, t_WADay.getzGross());
//			}
//			
//			//加一天
//			begin.add(Calendar.DAY_OF_MONTH, 1);
//		}
//		json.put("totalInLastYearMonth", totalInLastYearMonth);
//		json.put("totalOutLastYearMonth", totalOutLastYearMonth);
		List<T_WADay> lastYearInT_WADayList = null;
		List<T_WADay> lastYearOutT_WADayList = null;
		if(inMeterIDList.size() == 0){
			lastYearInT_WADayList = new ArrayList<T_WADay>();
		}else{
			lastYearInT_WADayList = indexService.getT_WADayBetween(inMeterIDList, TheBeginDate, TheEndDate);
		}
		
		if(outMeterIDList.size() == 0){
			lastYearOutT_WADayList = new ArrayList<T_WADay>();
		}else{
			lastYearOutT_WADayList = indexService.getT_WADayBetween(outMeterIDList, TheBeginDate, TheEndDate);
		}

		for (T_WADay t_WADay : lastYearInT_WADayList) {
			totalInLastYearMonth = CommonUtils
					.add(totalInLastYearMonth, t_WADay.getzGross() == null ? 0F : t_WADay.getzGross());
		}
		for (T_WADay t_WADay : lastYearOutT_WADayList) {
			totalOutLastYearMonth = CommonUtils
					.add(totalOutLastYearMonth, t_WADay.getzGross() == null ? 0F : t_WADay.getzGross());
		}
		
		if(totalInLastYearMonth == 0F){
			// 被除数为0 的情况
			json.put("loseRateLastYearMonth", 0);
		}else{
			float loseLastYearMonth = CommonUtils.subtract(totalInLastYearMonth, totalOutLastYearMonth);
			json.put("loseRateLastYearMonth", 
					CommonUtils.divide(loseLastYearMonth, totalInLastYearMonth, 2, BigDecimal.ROUND_UP));
		}
		return json;
	}
	
	//6
	private	JSONObject WAGross(String Meter_ID,String TheBeginDate,String TheEndDate)
	{
		JSONObject json = new JSONObject();
		//起始时间至终止时间内的进水量（如果日期内没有用量，就插入""请不要插入0,个数要对）
//		String[] jsonArrayIn = {"230","220","241","241","241","241","241","241","241","241","230","220","241","200","245","244","220","302","304","300","","","","","","","","","",""};
//		jsonArrayMonthGross.put("In", jsonArrayIn);
		// 解析开始 结束时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar begin = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			Date beginDate = sdf.parse(TheBeginDate);
			begin.setTime(beginDate);
			Date endDate = sdf.parse(TheEndDate);
			end.setTime(endDate);
		} catch (ParseException e) {
			json.put("result", "error");
			return json;
		}
		
		int meterIDInt = 0;
		try{
			meterIDInt = Integer.parseInt(Meter_ID);
		}catch(Exception e){
			json.put("result", "error");
			logger.error("参数异常:Meter_ID->"+Meter_ID);
			return json;
		}
	
		List<String> inList = new ArrayList<String>();
		List<Integer> meterIDList = new ArrayList<Integer>();
		meterIDList.add(meterIDInt);
		List<T_WADay> t_WADayList = indexService.getT_WADayBetween(meterIDList, TheBeginDate, TheEndDate);
		Iterator<T_WADay> iterator = null;
		StringBuffer indexStringBuffer = new StringBuffer();
		StringBuffer iteratorStringBuffer = new StringBuffer();
		boolean hasData = false;
		while(!begin.after(end)){
			// sb 设置为当前，如2018-1-11
			indexStringBuffer.setLength(0);
			indexStringBuffer.append(begin.get(Calendar.YEAR)).append("-")
				.append(begin.get(Calendar.MONTH)+1).append("-")
				.append(begin.get(Calendar.DAY_OF_MONTH));
			iterator = t_WADayList.iterator();
			hasData = false;
			while(iterator.hasNext()){
				T_WADay t_WADay = iterator.next();
				iteratorStringBuffer.setLength(0);
				iteratorStringBuffer.append(t_WADay.getSelectYear()).append("-")
				.append(t_WADay.getSelectMonth()).append("-")
				.append(t_WADay.getSelectDay());
				// 关键点 *************   这里主要是要找到这天的数据
				if(indexStringBuffer.toString().equals(iteratorStringBuffer.toString())){
					inList.add(t_WADay.getzGross() == null ? "0" : (t_WADay.getzGross()+""));
					iterator.remove();
					hasData = true;// 找到了
					break;
				}
			}
			
			// 没有找到这一天的数据
			if(!hasData){
				inList.add("0");
			}
			// 遍历下一天
			begin.add(Calendar.DAY_OF_MONTH, 1);
		}
		json.put("In", inList.toArray());
		return json;
	}

	//7
	private	JSONArray PRPressure(String Meter_ID,String TheBeginDate,String TheEndDate)
	{
		JSONArray jsonArray = new JSONArray();
		//起始时间至终止时间内的压力数据
		//select ValueTime,ZvalueZY  from Z_PR_201804 order by ValueTime
//		JSONArray jsonFlow = new JSONArray();
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 00:00");
//		jsonFlow.add( 116);
//		jsonArray.add(jsonFlow);
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 00:30");
//		jsonFlow.add(175);
//		jsonArray.add(jsonFlow);
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 01:00");
//		jsonFlow.add(180);
//		jsonArray.add(jsonFlow);
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 01:30");
//		jsonFlow.add(192);
//		jsonArray.add(jsonFlow);
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 02:00");
//		jsonFlow.add(193);
//		jsonArray.add(jsonFlow);
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 02:30");
//		jsonFlow.add(194);
//		jsonArray.add(jsonFlow);
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 03:00");
//		jsonFlow.add(180);
//		jsonArray.add(jsonFlow);
//		jsonFlow.clear();
//		jsonFlow.add( "2018-05-01 03:30");
//		jsonFlow.add(197);
//		jsonArray.add(jsonFlow);
		
		String[] tableNameSuffixArr = null;
		Date start = null;
		Date end = null;
		int meterIDInt = 0;
		try{
			meterIDInt = Integer.parseInt(Meter_ID);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			start = sdf.parse(TheBeginDate);
			end = sdf.parse(TheEndDate);
			tableNameSuffixArr = CommonUtils.getTableNameSuffix(start, end);
		}catch(Exception e){
			logger.error(e);
			logger.error("参数异常TheBeginDate->"+TheBeginDate+",TheEndDate->"+TheEndDate
					+",Meter_ID->"+Meter_ID);
			return jsonArray;
		}
		// 根据查询时间来没有符合的表
		if(tableNameSuffixArr.length == 0){
			return jsonArray;
		}
	
		JSONArray jsonFlow = new JSONArray();
		for(int i=0;i<1;i++){
			jsonFlow.clear();
			jsonFlow.add( "2018-05-01 00:00");
			jsonFlow.add( 116);
			jsonArray.add(jsonFlow);
		}
		

//		List<Map<String, Object>> flByMeterIDBetweenStartAndEnd = indexService.getPRByMeterIDBetweenStartAndEnd(meterIDInt, start, end, tableNameSuffixArr);
//		for (Map<String, Object> map : flByMeterIDBetweenStartAndEnd) {
//			jsonFlow.add(map.get("valuetime").toString());
//			jsonFlow.add(map.get("zvaluezy"));
//		}

		
		
		return jsonArray;
	}

//8
private	JSONArray FLFlow(String Meter_ID,String TheBeginDate,String TheEndDate)
{
	JSONArray jsonArray = new JSONArray();
	//起始时间至终止时间内的流量
	//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
	/*JSONArray jsonFlow = new JSONArray();
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 00:00");
	jsonFlow.add( 116);
	jsonArray.add(jsonFlow);
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 00:30");
	jsonFlow.add(175);
	jsonArray.add(jsonFlow);
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 01:00");
	jsonFlow.add(180);
	jsonArray.add(jsonFlow);
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 01:30");
	jsonFlow.add(192);
	jsonArray.add(jsonFlow);
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 02:00");
	jsonFlow.add(193);
	jsonArray.add(jsonFlow);
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 02:30");
	jsonFlow.add(194);
	jsonArray.add(jsonFlow);
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 03:00");
	jsonFlow.add(180);
	jsonArray.add(jsonFlow);
	jsonFlow.clear();
	jsonFlow.add( "2018-05-01 03:30");
	jsonFlow.add(197);
	jsonArray.add(jsonFlow);*/
	
	String[] tableNameSuffixArr = null;
	Date start = null;
	Date end = null;
	int meterIDInt = 0;
	try{
		meterIDInt = Integer.parseInt(Meter_ID);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		start = sdf.parse(TheBeginDate);
		end = sdf.parse(TheEndDate);
		tableNameSuffixArr = CommonUtils.getTableNameSuffix(start, end);
	}catch(Exception e){
		logger.error(e);
		logger.error("参数异常TheBeginDate->"+TheBeginDate+",TheEndDate->"+TheEndDate
				+",Meter_ID->"+Meter_ID);
		return jsonArray;
	}
	// 根据查询时间来没有符合的表
	if(tableNameSuffixArr.length == 0){
		return jsonArray;
	}
	List<Map<String, Object>> flByMeterIDBetweenStartAndEnd = indexService.getPRByMeterIDBetweenStartAndEnd(meterIDInt, start, end, tableNameSuffixArr);
	for (Map<String, Object> map : flByMeterIDBetweenStartAndEnd) {
		JSONArray jsonFlow = new JSONArray();
		jsonFlow.add(map.get("ValueTime"));
		jsonFlow.add(map.get("ZvalueZY"));
		jsonArray.add(jsonFlow);
	}
	return jsonArray;
}

	

	//9
	@RequestMapping(value="/SetIndexPageMeter",method=RequestMethod.GET) 
	@ResponseBody
	public String SetIndexPageMeter(HttpServletRequest req) throws IOException{
		String Area_ID = req.getParameter("Area_ID");
		String User_ID = req.getParameter("User_ID");//用户ID
		String Meter_ID = req.getParameter("Meter_ID");//用户ID
		
		int areaIDInt = 0;
		int meterIDInt = 0;
		try{
			areaIDInt = Integer.parseInt(Area_ID);
			meterIDInt = Integer.parseInt(Meter_ID);
		}catch(Exception e){
			logger.error(e);
			logger.error("参数异常：Area_ID->"+Area_ID+",User_ID->"+User_ID+",Meter_ID->"+Meter_ID);
			return CommonUtils.getFailJson().toString();
		}
		
		//将相应信息填入数据库IndexPageMeter表中注意判重
		IndexPageMeter indexPageMeter = new IndexPageMeter();
		indexPageMeter.setAreaID(areaIDInt);
		indexPageMeter.setUserID(User_ID);
		indexPageMeter.setMeterID(meterIDInt);
		IndexPageMeter indexPageMeterFromDB = indexService.getIndexPageMeter(indexPageMeter);
		// 数据库里面有这条记录，这里不用再插入了，直接返回成功
		if(indexPageMeterFromDB != null){
			return CommonUtils.getSuccessJson().toString();
		}
		// 数据库里面没有这条记录
		boolean result = indexService.addIndexPageMeter(indexPageMeter);
		if(result){
			return CommonUtils.getSuccessJson().toString();
		}
		return CommonUtils.getFailJson().toString();
	}
//2
	@RequestMapping(value="/LoadTopAreaList",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadTopAreaList(HttpServletRequest req) throws IOException{
	
		
//		String Area_ID = req.getParameter("Area_ID");
//		String Type = req.getParameter("Type");//Area,NetWork,WA....
		//ID,Num,Name
//		JSONArray jsonArray = new JSONArray();
//		JSONObject json1 = new JSONObject();
//		json1.put("ID", "4");
//		json1.put("Num", "0011");
//		json1.put("Name", "南湖");
//		jsonArray.add(json1);
//		json1.put("ID", "5");
//		json1.put("Num", "0011");
//		json1.put("Name", "东区");
//		jsonArray.add(json1);
//		json1.put("ID", "6");
//		json1.put("Num", "0012");
//		json1.put("Name", "北区");
//		jsonArray.add(json1);
//		json1.put("ID", "11");
//		json1.put("Num", "0014");
//		json1.put("Name", "南门");
//		jsonArray.add(json1);
//		json1.put("ID", "12");
//		json1.put("Num", "0015");
//		json1.put("Name", "东南门");
//		jsonArray.add(json1);
//		json1.put("ID", "13");
//		json1.put("Num", "0016");
//		json1.put("Name", "国交");
//		jsonArray.add(json1);
//		
//		
//		return jsonArray.toString();
		
		String Area_ID = req.getParameter("Area_ID");
		String Type = req.getParameter("Type");
		int areaIDInt = 0;
		try{
			areaIDInt = Integer.parseInt(Area_ID);
		}catch(Exception e){
			logger.error(e);
			logger.error("参数异常Area_ID->"+Area_ID);
			return CommonUtils.getFailJson().toString();
		}
		if(Type == null){
			logger.error("参数异常Type->"+Type);
			return CommonUtils.getFailJson().toString();
		}
		JSONArray retJsonArray = new JSONArray();
		// 区块
		if(Type.equals("Area")){
			List<Area> areaList = indexService.getAreasBySuperAreaID(areaIDInt);
			for(Area area : areaList){
				JSONObject json = new JSONObject();
				json.put("ID", area.getAreaID());
				json.put("Num", area.getAreaNum());
				json.put("Name", area.getAreaName());
				retJsonArray.add(json);
			}
			
			return retJsonArray.toString();
		}
		// 管网
		if(Type.equals("NetWork")){
			List<NetWork> netWorkList = indexService.getNetWorkByAreaID(areaIDInt);
			for(NetWork netWork : netWorkList){
				JSONObject json = new JSONObject();
				json.put("ID", netWork.getNetworkID());
				json.put("Num", netWork.getNetworkNum());
				json.put("Name", netWork.getNetworkName());
				retJsonArray.add(json);
			}
			
			return retJsonArray.toString();
		}
		// 水表
//		if(Type.equals("WA")){
//			indexService.getMeterByMeterType(meterType)
//			return null;
//		}
		if(Type.equals("WA") || Type.equals("PR") || Type.equals("FL") || 
				Type.equals("VALVE") || Type.equals("POOL") || Type.equals("PUMP")){
			List<Meter> meterList = indexService.getMeterByAreaIDAndMeterType(areaIDInt, Type);
			for (Meter meter : meterList) {
				JSONObject json = new JSONObject();
				json.put("ID", meter.getMeterID());
				json.put("Num", meter.getMeterNum());
				json.put("Name", meter.getMeterName());
				retJsonArray.add(json);
			}
			return retJsonArray.toString();
		}
		// 如果一个都不符合，证明传入参数有问题，这里返回失败json
		return CommonUtils.getFailJson().toString();
		
	}

	@RequestMapping(value="/LoadTopCharts",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadTopCharts(HttpServletRequest req) throws IOException, ParseException{
		String Area_ID = req.getParameter("Area_ID");
		String TheDate = req.getParameter("TheDate");
		
		if(TheDate==null || TheDate.isEmpty())
		{
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd 00:00"); //加上时间
			Date dateS=new Date();
			TheDate=dateFormat.format(dateS);
		}
		else
		{
			if(! TheDate.contains(":"))
			{//去除时分秒
				TheDate+=" 00:00";
			}
			
		}
	
		JSONObject rObject= new JSONObject();
		rObject.put("Gross72Hour", Gross72Hour(Area_ID,TheDate));
		rObject.put("Gross2Month", Gross2Month(Area_ID,TheDate));
		rObject.put("GrossYear", GrossYear(Area_ID,TheDate));
		rObject.put("GrossNight", GrossNight(Area_ID,TheDate));
		return rObject.toString();
	}

//10
	//72小时用水
	private	JSONArray Gross72Hour(String Area_ID,String TheDate)
	{
		//String StartTime,String EndTime
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:00"); //加上时间
		Calendar calendar = Calendar.getInstance();
		Date dateS=new Date();
		try {
			dateS = dateFormat.parse(TheDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		calendar.setTime(dateS);
		calendar.add(Calendar.HOUR, 71);
		Date dateE=calendar.getTime();//起始数据要取前一个小时的

		//时间为 TheDate前一天0点到TheDate当天23点
		JSONArray jsonArray = new JSONArray();

		try {
			jsonArray=	gross.WAHourGross(Area_ID,dateFormat.format(dateS),dateFormat.format(dateE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArray;

	}
	//两月用水
//两月用水

//11
private	JSONArray Gross2Month(String Area_ID,String TheDate) throws ParseException
{
	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-01 00:00"); //加上时间
	SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy-MM-dd 00:00"); //加上时间
	Calendar calendar = Calendar.getInstance();
	Date dateS=new Date();
	try {
		dateS = dateFormat.parse(TheDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	calendar.setTime(dateS);
	calendar.add(Calendar.MONTH, -1);
	String StartTime=dateFormat.format(calendar.getTime());
	dateS = dateFormat.parse(StartTime);
	calendar.setTime(dateS);
	calendar.add(Calendar.MONTH, 2);

	calendar.add(Calendar.DATE, -1);
	Date dateE=calendar.getTime();//起始数据要取前一个小时的
	String EndTime=dateFormat1.format(calendar.getTime());
	JSONObject JDAy = 	gross.AreaDayGross(Area_ID,StartTime,EndTime);
	
	
	
	JSONArray jsonArray = new JSONArray();
	

	jsonArray.add(JDAy.get("X"));
	jsonArray.add(JDAy.get("In"));

	
	return jsonArray;
}
//本年用水

//12
private	JSONArray GrossYear(String Area_ID,String TheDate) throws ParseException
{
	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-01-01 00:00"); //加上时间
	SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy-12-01 00:00"); //加上时间
	Calendar calendar = Calendar.getInstance();
	Date dateS=new Date();
	try {
		dateS = dateFormat.parse(TheDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	String StartTime=dateFormat.format(calendar.getTime());
	String EndTime=dateFormat1.format(calendar.getTime());
	
	JSONObject JDAy = 	gross.AreaMonthGross(Area_ID,StartTime,EndTime);
	

	JSONArray jsonArray = new JSONArray();
	

	jsonArray.add(JDAy.get("X"));
	jsonArray.add(JDAy.get("In"));
	return jsonArray;
	
}

//13
//最小夜间流量
private	JSONArray GrossNight(String Area_ID,String TheDate) throws ParseException
{
	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-01 00:00"); //加上时间
	SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy-MM-dd 00:00"); //加上时间
	Calendar calendar = Calendar.getInstance();
	Date dateS=new Date();
	try {
		dateS = dateFormat.parse(TheDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	String StartTime=dateFormat.format(calendar.getTime());
	dateS = dateFormat.parse(StartTime);
	calendar.setTime(dateS);
	calendar.add(Calendar.MONTH, 2);

	calendar.add(Calendar.DATE, -1);
	Date dateE=calendar.getTime();//起始数据要取前一个小时的
	String EndTime=dateFormat1.format(calendar.getTime());
	JSONObject JDAy = 	gross.AreaDayGross(Area_ID,StartTime,EndTime);
	
	
	
	JSONArray jsonArray = new JSONArray();
	

	jsonArray.add(JDAy.get("X"));
	jsonArray.add(JDAy.get("Min"));

	
	return jsonArray;
}

//	@RequestMapping(value="/indexService",method=RequestMethod.GET) 
//	@ResponseBody
//	public String indexService(HttpServletRequest req) throws IOException{
//		//JSONObject jsonObject = 
//		
//		String Area_ID = req.getParameter("Area_ID");
//		String Type = req.getParameter("Type");//Area,NetWork,WA....
//		//ID,Num,Name
//		JSONArray jsonArray = new JSONArray();
//		JSONObject json1 = new JSONObject();
//		json1.put("ID", "4");
//		json1.put("Num", "0011");
//		json1.put("Name", "南湖");
//		jsonArray.add(json1);
//		json1.put("ID", "5");
//		json1.put("Num", "0011");
//		json1.put("Name", "东区");
//		jsonArray.add(json1);
//		json1.put("ID", "6");
//		json1.put("Num", "0012");
//		json1.put("Name", "北区");
//		jsonArray.add(json1);
//		json1.put("ID", "11");
//		json1.put("Num", "0014");
//		json1.put("Name", "南门");
//		jsonArray.add(json1);
//		json1.put("ID", "12");
//		json1.put("Num", "0015");
//		json1.put("Name", "东南门");
//		jsonArray.add(json1);
//		json1.put("ID", "13");
//		json1.put("Num", "0016");
//		json1.put("Name", "国交");
//		jsonArray.add(json1);
//		
//		
//		return jsonArray.toString();
//	}
	
	//15
	//区块主页显示设备列表及详细信息
	@RequestMapping(value="/LoadIndexPageMeter",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadIndexPageMeter(HttpServletRequest req){

		String Area_ID = req.getParameter("Area_ID");
		String User_ID = req.getParameter("User_ID");//用户ID
		int areaIDInt = 0;
		try{
			areaIDInt = Integer.parseInt(Area_ID);
		}catch(Exception e){
			logger.error(e);
			logger.error("请求参数异常Area_ID->"+Area_ID);
			return CommonUtils.getFailJson().toString();
		}
		
		//select a.Meter_ID,b.Meter_Type,Meter_Num,Beilv,Xiuzheng,b.Meter_Name,Meter_485Address,Meter_Changjia,
		//convert(varchar(16),Meter_Time,120)Meter_Time,Meter_Value1,Meter_Value2,Meter_Value3,Meter_Value4  
		//from IndexPageMeter a,Meter b where a.Meter_ID=b.Meter_ID
		/*JSONArray jsonArray = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.put("Meter_ID", "39");
		json1.put("Meter_Type", "WA");
		json1.put("Meter_Num", "001");
		json1.put("Meter_Name", "测试水表1");
		json1.put("Beilv", "1");
		json1.put("Xiuzheng", "0");
		json1.put("Meter_485Address", "00122356455");
		json1.put("Meter_Changjia", "盛帆电子");
		json1.put("Meter_Time", "2018-05-07 17:04");
		json1.put("Meter_Value1", "159");
		json1.put("Meter_Value2", "0");
		json1.put("Meter_Value3", "0");
		json1.put("Meter_Value4", "0");
		json1.put("MonthPressure", "[]");
		
		jsonArray.add(json1);
		json1.put("Meter_ID", "40");
		json1.put("Meter_Type", "PR");
		json1.put("Meter_Num", "001");
		json1.put("Beilv", "1");
		json1.put("Xiuzheng", "0");
		json1.put("Meter_Name", "压力计1");
		json1.put("Meter_485Address", "00122356455");
		json1.put("Meter_Changjia", "盛帆电子");
		json1.put("Meter_Time", "2018-05-07 17:04");
		json1.put("Meter_Value1", "0");
		json1.put("Meter_Value2", "0");
		json1.put("Meter_Value3", "0");
		json1.put("Meter_Value4", "0");
		//如果为压力计请加载该表当月的瞬时压力
		JSONArray JPRPressure =PRPressure ( "112", "2018-05-01", "2018-05-31");
		json1.put("MonthPressure", JPRPressure);
		jsonArray.add(json1);
		
		json1.put("Meter_ID", "41");
		json1.put("Meter_Type", "FL");
		json1.put("Meter_Num", "001");
		json1.put("Beilv", "1");
		json1.put("Xiuzheng", "0");
		json1.put("Meter_Name", "流量计1");
		json1.put("Meter_485Address", "00122356455");
		json1.put("Meter_Changjia", "盛帆电子");
		json1.put("Meter_Time", "2018-05-07 17:04");
		json1.put("Meter_Value1", "0");
		json1.put("Meter_Value2", "0");
		json1.put("Meter_Value3", "0");
		json1.put("Meter_Value4", "0");
		json1.put("MonthPressure", "[]");
		jsonArray.add(json1);
		json1.put("Meter_ID", "43");
		json1.put("Meter_Type", "WA");
		json1.put("Meter_Num", "001");
		json1.put("Meter_Name", "测试水表1");
		json1.put("Beilv", "1");
		json1.put("Xiuzheng", "0");
		json1.put("Meter_485Address", "00122356455");
		json1.put("Meter_Changjia", "盛帆电子");
		json1.put("Meter_Time", "");
		json1.put("Meter_Value1", "");
		json1.put("Meter_Value2", "0");
		json1.put("Meter_Value3", "0");
		json1.put("Meter_Value4", "0");
		json1.put("MonthPressure", "[]");
		
		jsonArray.add(json1);
		json1.put("Meter_ID", "114");
		json1.put("Meter_Type", "PR");
		json1.put("Meter_Num", "001");
		json1.put("Beilv", "1");
		json1.put("Xiuzheng", "0");
		json1.put("Meter_Name", "压力计1");
		json1.put("Meter_485Address", "00122356455");
		json1.put("Meter_Changjia", "盛帆电子");
		json1.put("Meter_Time", "2018-05-07 17:04");
		json1.put("Meter_Value1", "0");
		json1.put("Meter_Value2", "0");
		json1.put("Meter_Value3", "0");
		json1.put("Meter_Value4", "0");
		//如果为压力计请加载该表当月的瞬时压力
		 JPRPressure =PRPressure ( "112", "2018-05-01", "2018-05-31");
		json1.put("MonthPressure", JPRPressure);
		jsonArray.add(json1);
		
		json1.put("Meter_ID", "115");
		json1.put("Meter_Type", "FL");
		json1.put("Meter_Num", "001");
		json1.put("Beilv", "1");
		json1.put("Xiuzheng", "0");
		json1.put("Meter_Name", "流量计1");
		json1.put("Meter_485Address", "00122356455");
		json1.put("Meter_Changjia", "盛帆电子");
		json1.put("Meter_Time", "2018-05-07 17:04");
		json1.put("Meter_Value1", "0");
		json1.put("Meter_Value2", "0");
		json1.put("Meter_Value3", "0");
		json1.put("Meter_Value4", "0");
		json1.put("MonthPressure", "[]");
		jsonArray.add(json1);*/
		
		JSONArray jsonArray = new JSONArray();
		List<Meter> meterList = indexService.getMeterFromIndexPageMeterID(areaIDInt,User_ID);
		for (Meter meter : meterList) {
			JSONObject meterJson = new JSONObject();
			meterJson.put("Meter_ID", meter.getMeterID() == null ? "" : meter.getMeterID());
			meterJson.put("Meter_Type", meter.getMeterType() == null ? "" : meter.getMeterType());
			meterJson.put("Meter_Num", meter.getMeterNum() == null ? "" : meter.getMeterNum());
			meterJson.put("Beilv", meter.getBeilv() == null ? "" : meter.getBeilv());
			meterJson.put("Xiuzheng", meter.getXiuzheng() == null ? "" : meter.getXiuzheng());
			meterJson.put("Meter_Name", meter.getMeterName() == null ? "" : meter.getMeterName());
			meterJson.put("Meter_485Address", meter.getMeter485Address() == null ? "" : meter.getMeter485Address());
			meterJson.put("Meter_Changjia", meter.getMeterChangjia() == null ? "" : meter.getMeterChangjia());
			if(meter.getMeterTime() == null){
				meterJson.put("Meter_Time", "");
			}else{
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String meterTimeStr = sdf.format(meter.getMeterTime());
					meterJson.put("Meter_Time", meterTimeStr);
				}catch(Exception e){
					logger.error(e);
					logger.error("meterTime解析异常，现在把meterTime设置为空字符串");
					meterJson.put("Meter_Time", "");
				}
			}
			meterJson.put("Meter_Value1", meter.getMeterValue1() == null ? "" : meter.getMeterValue1());
			meterJson.put("Meter_Value2", meter.getMeterValue2() == null ? "" : meter.getMeterValue2());
			meterJson.put("Meter_Value3", meter.getMeterValue3() == null ? "" : meter.getMeterValue3());
			meterJson.put("Meter_Value4", meter.getMeterValue4() == null ? "" : meter.getMeterValue4());
			
			//如果为压力计请加载该表当月的瞬时压力
			if(meter.getMeterType() != null && meter.getMeterType().equals("PR") &&
					meter.getMeterID() != null){
				Date nowDate = new Date();
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				start.setTime(nowDate);
				end.setTime(nowDate);
				// 设置开始时间为本月初，结束时间为本月末
				start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
				end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startDateStr = sdf.format(start.getTime());
				String endDateStr = sdf.format(end.getTime());
				JSONArray JPRPressure = PRPressure( meter.getMeterID().toString(), startDateStr, endDateStr);
				meterJson.put("MonthPressure", JPRPressure);
			}else{
				meterJson.put("MonthPressure", new JSONArray());
			}
			jsonArray.add(meterJson);
		}
		
		return jsonArray.toString();
	}

	//16
	//区块漏损信息5个饼图地方
	@RequestMapping(value="/LoadAreaLoseInfo",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadAreaLoseInfo(HttpServletRequest req) throws IOException{

		String Area_ID = req.getParameter("Area_ID");
		String TheDate = req.getParameter("TheDate");

//		JSONObject json1 = new JSONObject();
//		json1.put("DayLoseRa", "86");//日漏损率
//		json1.put("MonthLoseRa", "23");//月漏损率
//		json1.put("YearLoseRa", "57");//年漏损率
//		json1.put("BJLoseRa", "34");//背景漏失占比
//		json1.put("DWGCLoseRa", "81");//单位管长漏损
//		
//		return json1.toString();
		int areaIDInt = 0;
		int selectYear = 0;
		int selectMonth = 0;
		int selectDay = 0;
		try{
			areaIDInt = Integer.parseInt(Area_ID);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Calendar selectCalendar = Calendar.getInstance();
			selectCalendar.setTime(sdf.parse(TheDate));
			selectYear = selectCalendar.get(Calendar.YEAR);
			selectMonth = selectCalendar.get(Calendar.MONTH) + 1;
			selectDay = selectCalendar.get(Calendar.DAY_OF_MONTH);
		}catch(Exception e){
			logger.error(e);
			logger.error("请求参数异常Area_ID->"+Area_ID+",TheDate->"+TheDate);
			return CommonUtils.getFailJson().toString();
		}
		// 这里查询准确到日
		Map<String, Object> sumGrossDayMap = 
				indexService.getSumGrossInAndGrossOut(areaIDInt, selectYear, selectMonth, selectDay);
		// 这里查询准确到月
		Map<String, Object> sumGrossMonthMap = 
				indexService.getSumGrossInAndGrossOut(areaIDInt, selectYear, selectMonth, null);
		// 这里查询准确到年
		Map<String, Object> sumGrossYearMap = 
				indexService.getSumGrossInAndGrossOut(areaIDInt, selectYear, null, null);
		float daySumGrossIn = 0F;
		float daySumGrossOut = 0F;
		float monthSumGrossIn = 0F;
		float monthSumGrossOut = 0F;
		float yearSumGrossIn = 0F;
		float yearSumGrossOut = 0F;
		try{
			if(sumGrossDayMap == null){
				daySumGrossIn = 0;
				daySumGrossOut = 0;
			}else{
				daySumGrossIn = 
						 (sumGrossDayMap.get("SumIn") == null ? 0 : ((BigDecimal)sumGrossDayMap.get("SumIn")).floatValue());
				daySumGrossOut = 
						 (sumGrossDayMap.get("SumOut") == null ? 0 : ((BigDecimal)sumGrossDayMap.get("SumOut")).floatValue());
			}
			if(sumGrossMonthMap == null){
				monthSumGrossIn = 0;
				monthSumGrossOut = 0;
			}else{
				monthSumGrossIn = 
						 (sumGrossMonthMap.get("SumIn") == null ? 0 : ((BigDecimal)sumGrossMonthMap.get("SumIn")).floatValue());
				monthSumGrossOut = 
						 (sumGrossMonthMap.get("SumOut") == null ? 0 : ((BigDecimal)sumGrossMonthMap.get("SumOut")).floatValue());
			}
			if(sumGrossYearMap == null){
				yearSumGrossIn = 0;
				yearSumGrossOut = 0;
			}else{
				yearSumGrossIn = 
						(sumGrossYearMap.get("SumIn") == null ? 0 : ((BigDecimal)sumGrossYearMap.get("SumIn")).floatValue());
				yearSumGrossOut = 
						(sumGrossYearMap.get("SumOut") == null ? 0 : ((BigDecimal)sumGrossYearMap.get("SumOut")).floatValue());
			}
//			daySumGrossIn = Float.parseFloat(daySumGrossInStr);
//			daySumGrossOut = Float.parseFloat(daySumGrossOutStr);
//			monthSumGrossIn = Float.parseFloat(monthSumGrossInStr);
//			monthSumGrossOut = Float.parseFloat(monthSumGrossOutStr);
//			yearSumGrossIn = Float.parseFloat(yearSumGrossInStr);
//			yearSumGrossOut = Float.parseFloat(yearSumGrossOutStr);
		}catch(Exception e){
			logger.error(e);
			logger.error("查询T_AreaDay里面的总进出水量有问题");
		}
		float dayLose = CommonUtils.subtract(daySumGrossIn, daySumGrossOut);
		float monthLose = CommonUtils.subtract(monthSumGrossIn, monthSumGrossOut);
		float yearLose = CommonUtils.subtract(yearSumGrossIn, yearSumGrossOut);
		float dayLoseRate = daySumGrossIn == 0 ? 0 : CommonUtils.divide(dayLose, daySumGrossIn, 4, BigDecimal.ROUND_UP);
		float monthLoseRate = monthSumGrossIn == 0 ? 0 : CommonUtils.divide(monthLose, monthSumGrossIn, 4, BigDecimal.ROUND_UP);
		float yearLoseRate = yearSumGrossIn == 0 ? 0 : CommonUtils.divide(yearLose, yearSumGrossIn, 4, BigDecimal.ROUND_UP);

		// 乘以100，%
		dayLoseRate = CommonUtils.multiply(dayLoseRate, 100);
		monthLoseRate = CommonUtils.multiply(monthLoseRate, 100);
		yearLoseRate = CommonUtils.multiply(yearLoseRate, 100);
		//转为字符串，不然float直接放进json里面会出现数字变成0.987678888之类乱七八糟的数据
		String dayLoseRateStr = String.valueOf(dayLoseRate);
		String monthLoseRateStr = String.valueOf(monthLoseRate);
		String yearLoseRateStr = String.valueOf(yearLoseRate);
		JSONObject json = new JSONObject();
		json.put("DayLoseRa", dayLoseRateStr);//日漏损率
		json.put("MonthLoseRa", monthLoseRateStr);//月漏损率
		json.put("YearLoseRa", yearLoseRateStr);//年漏损率
		json.put("BJLoseRa", "100");//背景漏失占比
		json.put("DWGCLoseRa", "100");//单位管长漏损
		
		return json.toString();
	}

		//17 区块异常信息列表
		@RequestMapping(value="/LoadErrList",method=RequestMethod.GET) 
		@ResponseBody
		public String LoadErrList(HttpServletRequest req){
		//显示当天的即可
		//select Err_ID,Area_ID,Err_Type,'管网漏损' TypeName,Err_Title,Err_Time  from ErrList  
		//Err_Type:1:管道漏损,2,区块水平衡异常
			String Area_ID = req.getParameter("Area_ID");
			String Type = req.getParameter("Type");//0：查询所有，其他值筛选ErrList中的Err_Type
			//Type为空就当0处理
			//ID,Num,Name
//			JSONArray jsonArray = new JSONArray();
//			JSONObject json1 = new JSONObject();
//			json1.put("Err_ID", "4");
//			json1.put("Area_ID", "1");
//			json1.put("Err_Type", "1");
//			json1.put("TypeName", "管道漏损");
//			json1.put("Err_Title", "2018-05-14 管道漏损，进水200，出水100，漏损率：50%");
//			json1.put("Err_Time", "2018-05-14 10:20");
//			
//			jsonArray.add(json1);
//			JSONObject json2 = new JSONObject();
//			json2.put("Err_ID", "5");
//			json2.put("Area_ID", "1");
//			json2.put("Err_Type", "2");
//			json2.put("TypeName", "区块水平衡异常");
//			json2.put("Err_Title", "2018-05-14 区块水平衡异常，进水200，出水100，漏损率：50%");
//			json2.put("Err_Time", "2018-05-14 10:20");
//			
//			jsonArray.add(json2);
			int areaIDInt = 0;
			try{
				areaIDInt = Integer.parseInt(Area_ID);
				if(Type == null){
					throw new Exception();
				}
				
				if(Type.equals("0")){
					// type为0，代表查询所有，这个字段设为null，就可以
					Type = null;
				}
			}catch(Exception e){
				logger.error(e);
				logger.error("请求参数异常Area_ID->"+Area_ID+",Type->"+Type);
				return CommonUtils.getFailJson().toString();
			}
			
			List<Map<String, Object>> errMapList = indexService.getErrListByAreaIDAndErrType(areaIDInt, Type);
			
			JSONArray jsonArray = new JSONArray();
			for (Map<String, Object> errMap : errMapList) {
				JSONObject json = new JSONObject();
				json.put("Err_ID", errMap.get("Err_ID") == null ? "" : errMap.get("Err_ID"));
				json.put("Area_ID", errMap.get("Area_ID") == null ? "" : errMap.get("Area_ID"));
				json.put("Err_Type", errMap.get("Err_Type") == null ? "" : errMap.get("Err_Type"));
				json.put("TypeName", errMap.get("TypeName") == null ? "" : errMap.get("TypeName"));
				json.put("Err_Title", errMap.get("Err_Title") == null ? "" : errMap.get("Err_Title"));
				json.put("Err_Time", errMap.get("Err_Time") == null ? "" : errMap.get("Err_Time"));
				jsonArray.add(json);
			}

			return jsonArray.toString();
		}
}
