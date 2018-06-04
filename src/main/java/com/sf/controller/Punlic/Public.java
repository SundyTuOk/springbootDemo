package com.sf.controller.Punlic;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.bean.Area;
import com.sf.bean.ZWA;
import com.sf.controller.Gross.Gross;
import com.sf.dao.AreaDAO;
import com.sf.dao.MeterDAO;
import com.sf.utils.CommonUtils;

@Controller
@RequestMapping("/Public")
public class Public {
	
		@Resource(name = "areaDAO")
		private AreaDAO areaDAO;
		
		@Resource(name = "meterDAO")
		private MeterDAO meterDAO;

		@Resource(name = "gross")
		private Gross gross;
	//1
		@RequestMapping(value="/LoadLeftMenu",method=RequestMethod.GET) 
		@ResponseBody
		public String LoadLeftMenu(HttpServletRequest req) throws IOException{
		
			String User_ID = req.getParameter("User_ID");
			JSONArray PageChild = new JSONArray();
			JSONArray jsonArray = new JSONArray();
			JSONObject json1 = new JSONObject();
			json1.put("PageNum", "K001");
			json1.put("PageName", "首页");
			json1.put("PageUrl", "index.html");
			json1.put("PageChild", "[]");
			jsonArray.add(json1);
			json1.put("PageNum", "K101");
			json1.put("PageName", "管网模型");
			json1.put("PageUrl", "K101.html");
			json1.put("PageChild", "[]");
			jsonArray.add(json1);
			
			json1.put("PageNum", "K200");
			json1.put("PageName", "水平衡分析");
			json1.put("PageUrl", "");
			PageChild = new JSONArray();
			JSONObject json11 = new JSONObject();
/*			json11.put("PageNum", "K201");
			json11.put("PageName", "水平衡模型");
			json11.put("PageUrl", "K201.html");
			json11.put("PageChild", "[]");
			PageChild.add(json11);*/
			json11.put("PageNum", "K202");
			json11.put("PageName", "水平衡测试");
			json11.put("PageUrl", "K202.html");
			json11.put("PageChild", "[]");
			PageChild.add(json11);
			json1.put("PageChild", PageChild);
			jsonArray.add(json1);

			json1.put("PageNum", "K401");
			json1.put("PageName", "异常报警");
			json1.put("PageUrl", "K401.html");
			json1.put("PageChild", "[]");
			jsonArray.add(json1);
			
			
			json1.put("PageNum", "K300");
			json1.put("PageName", "系统参数");
			json1.put("PageUrl", "");
			PageChild = new JSONArray();
			json11 = new JSONObject();
			json11.put("PageNum", "K301");
			json11.put("PageName", "区块管理");
			json11.put("PageUrl", "K301.html");
			json11.put("PageChild", "[]");
			PageChild.add(json11);
			json11.put("PageNum", "K302");
			json11.put("PageName", "管网管理");
			json11.put("PageUrl", "K302.html");
			json11.put("PageChild", "[]");
			PageChild.add(json11);
			json11.put("PageNum", "K303");
			json11.put("PageName", "设备档案");
			json11.put("PageUrl", "K303.html");
			json11.put("PageChild", "[]");
			PageChild.add(json11);
			json1.put("PageChild", PageChild);
			jsonArray.add(json1);
			
		
			return jsonArray.toString();
		}
		
		//2
		//用户详细信息
		@RequestMapping(value="/LoadUserInfo",method=RequestMethod.GET) 
		@ResponseBody
		public String LoadUserInfo(HttpServletRequest req) throws IOException{

			String User_ID = req.getParameter("User_ID");

			JSONObject json1 = new JSONObject();
			json1.put("User_ID", "1");
			json1.put("User_Num", "001");
			json1.put("User_Name", "系统管理员");
			json1.put("User_Photo", "img/profile-pic.jpg");
				
			return json1.toString();
		}
		//3 系统报警提要
		@RequestMapping(value="/LoadLeftErrList",method=RequestMethod.GET) 
		@ResponseBody
		public String LoadLeftErrList(HttpServletRequest req) throws IOException{
		
			String Area_ID = req.getParameter("Area_ID");
			JSONArray jsonArray = new JSONArray();
			JSONObject json1 = new JSONObject();
			json1.put("Err_Type", "1");
			json1.put("TypeName", "管道漏损");
			json1.put("Count", "1");
			jsonArray.add(json1);

			json1.put("Err_Type", "2");
			json1.put("TypeName", "区块水平衡异常");
			json1.put("Count", "1");
			jsonArray.add(json1);
			return jsonArray.toString();
		}
		//4 设备运行状况
		@RequestMapping(value="/LoadLeftMeterState",method=RequestMethod.GET) 
		@ResponseBody
		public String LoadLeftMeterState(HttpServletRequest req) throws IOException{
		//qualified
			String Area_ID = req.getParameter("Area_ID");
			
			
			JSONObject json1 = new JSONObject();
			json1.put("WModeQ", "100");//水平衡合格率
			json1.put("WAOnline", "60");//水表在线率
			json1.put("FLOnline", "20");//流量计在线率
			json1.put("PROnline", "80");//流量计在线率

			return json1.toString();
		}
		//5
		//区块详细信息
		@RequestMapping(value="/LoadPageAreaInfo",method=RequestMethod.GET) 
		@ResponseBody
		public String LoadPageAreaInfo(HttpServletRequest req) throws IOException{
			JSONObject json1 = new JSONObject();
			//select Area_ID,Area_Num,Area_Name,DLMode_FileAddress,JGMode_FileAddress from Area
			String Area_ID = req.getParameter("Area_ID");
			//如果Area_ID为空或者为0请查询系统中第一个父节点为0的区块ID
			int areaID = 0;
			if(Area_ID != null && !Area_ID.isEmpty()){
				areaID=Integer.parseInt(Area_ID);
			}
			
			List<Area>  areaList = areaDAO.getAreasBySuperiorAreaID(areaID);
			if(areaList.size()>0)//没有数据就回空
			{
				json1.put("Area_ID", areaList.get(0).getAreaID().toString());
				json1.put("Area_Num", areaList.get(0).getAreaNum());
				json1.put("Area_Name", areaList.get(0).getAreaName());
				json1.put("DLMode_FileAddress","XMLData/"+ areaList.get(0).getDLModeFileAddress());
				json1.put("JGMode_FileAddress", "XMLData/"+areaList.get(0).getJGModeFileAddress());
			}

			return json1.toString();
		}

		//6
		//测试入口
		@RequestMapping(value="/CSCCC",method=RequestMethod.GET) 
		@ResponseBody
		public String CSCCC(HttpServletRequest req) throws IOException{

			String Area_ID = req.getParameter("Area_ID");
			String TheDate = req.getParameter("TheDate");
			if(TheDate==null || TheDate.isEmpty())
			{
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //加上时间
				Date dateS=new Date();
				TheDate=dateFormat.format(dateS);
			}
			
			JSONObject rObject= new JSONObject();
			rObject.put("Gross72Hour", Gross72Hour(Area_ID,TheDate));
			rObject.put("Gross2Month", Gross2Month(Area_ID,TheDate));
			rObject.put("GrossYear", GrossYear(Area_ID,TheDate));
			rObject.put("GrossNight", GrossNight(Area_ID,TheDate));
			return rObject.toString();
			
	
		}
	

		@RequestMapping(value="/LoadTopCharts",method=RequestMethod.GET) 
		@ResponseBody
		public String LoadTopCharts(HttpServletRequest req) throws IOException{
			String Area_ID = req.getParameter("Area_ID");
			String TheDate = req.getParameter("TheDate");
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
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd 00:00"); //加上时间
		Calendar calendar = Calendar.getInstance();
		Date dateS=new Date();
		
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

	//11
	private	JSONArray Gross2Month(String Area_ID,String TheDate)
	{
		
		
		//时间为 TheDate前一天0点到TheDate当天23点
		JSONArray jsonArray = new JSONArray();
		
		
		
		//起始时间至终止时间内的流量
		//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
		JSONArray jsonFlow = new JSONArray();
		JSONArray jsonFlowX = new JSONArray();

		jsonFlowX.add( "2018-05-01");
		jsonFlow.add( 116);
		
		jsonFlowX.add( "2018-05-02");
		jsonFlow.add(175);


		jsonFlowX.add( "2018-05-03");
		jsonFlow.add(180);

		jsonFlowX.add( "2018-05-04");
		jsonFlow.add(192);

		jsonFlowX.add( "2018-05-05");
		jsonFlow.add(193);

		jsonFlowX.add( "2018-05-06");
		jsonFlow.add(194);

		jsonFlowX.add( "2018-05-07");
		jsonFlow.add(180);


		jsonFlowX.add( "2018-05-08");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-09");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-10");
		jsonFlow.add(150);
		
		jsonFlowX.add( "2018-05-11");
		jsonFlow.add(151);

		jsonFlowX.add( "2018-05-12");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-13");
		jsonFlow.add(184);

		jsonFlowX.add( "2018-05-14");
		jsonFlow.add(197);
		
		jsonArray.add(jsonFlow);
		jsonArray.add(jsonFlowX);
		
		return jsonArray;
	}
	//本年用水

	//12
	private	JSONArray GrossYear(String Area_ID,String TheDate)
	{
		//时间为 TheDate前一天0点到TheDate当天23点
		JSONArray jsonArray = new JSONArray();
		//起始时间至终止时间内的流量
		//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
		JSONArray jsonFlow = new JSONArray();
		JSONArray jsonFlowX = new JSONArray();

		jsonFlowX.add( "2018-05-01");
		jsonFlow.add( 116);

		jsonFlowX.add( "2018-05-02");
		jsonFlow.add(175);

		jsonFlowX.add( "2018-05-03");
		jsonFlow.add(180);

		jsonFlowX.add( "2018-05-04");
		jsonFlow.add(192);

		jsonFlowX.add( "2018-05-05");
		jsonFlow.add(193);

		jsonFlowX.add( "2018-05-06");
		jsonFlow.add(194);

		jsonFlowX.add( "2018-05-07");
		jsonFlow.add(180);

		jsonFlowX.add( "2018-05-08");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-09");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-10");
		jsonFlow.add(150);

		jsonFlowX.add( "2018-05-11");
		jsonFlow.add(151);

		jsonFlowX.add( "2018-05-12");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-13");
		jsonFlow.add(184);

		jsonFlowX.add( "2018-05-14");
		jsonFlow.add(197);
		
		jsonArray.add(jsonFlow);
		jsonArray.add(jsonFlowX);
		return jsonArray;
	}

	//13
	//最小夜间流量
	private	JSONArray GrossNight(String Area_ID,String TheDate)
	{
		//时间为 TheDate前一天0点到TheDate当天23点
		JSONArray jsonArray = new JSONArray();
		//起始时间至终止时间内的流量
		//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
		JSONArray jsonFlow = new JSONArray();
		JSONArray jsonFlowX = new JSONArray();

		jsonFlowX.add( "2018-05-01");
		jsonFlow.add( 116);

		jsonFlowX.add( "2018-05-02");
		jsonFlow.add(175);

		jsonFlowX.add( "2018-05-03");
		jsonFlow.add(180);

		jsonFlowX.add( "2018-05-04");
		jsonFlow.add(192);

		jsonFlowX.add( "2018-05-05");
		jsonFlow.add(193);

		jsonFlowX.add( "2018-05-06");
		jsonFlow.add(194);

		jsonFlowX.add( "2018-05-07");
		jsonFlow.add(180);

		jsonFlowX.add( "2018-05-08");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-09");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-10");
		jsonFlow.add(150);

		jsonFlowX.add( "2018-05-11");
		jsonFlow.add(151);

		jsonFlowX.add( "2018-05-12");
		jsonFlow.add(197);

		jsonFlowX.add( "2018-05-13");
		jsonFlow.add(184);

		jsonFlowX.add( "2018-05-14");
		jsonFlow.add(197);
		
		jsonArray.add(jsonFlow);
		jsonArray.add(jsonFlowX);
		return jsonArray;
	}

}
