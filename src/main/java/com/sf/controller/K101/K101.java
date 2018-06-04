package com.sf.controller.K101;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/K101")
public class K101 {
	//1 获取水平衡折线图列表
	@RequestMapping(value="/LoadWTestList",method=RequestMethod.GET) 
	@ResponseBody
	public String LoadWTestList(HttpServletRequest req) throws IOException{
		//select top 4 WTestParameter_ID,Name from WTestParameter where Enable=1 order by OrderIndex
//最多显示4个
		String Area_ID = req.getParameter("Area_ID");
		String TheBeginDate = req.getParameter("TheBeginDate");//2018-05-01
		String TheEndDate = req.getParameter("TheEndDate");//2018-06-01
		
		JSONObject PageChild = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.put("WTestParameter_ID", "1");
		json1.put("Name", "一级水平衡");
		PageChild=	WTestList("1",TheBeginDate,TheEndDate);
		json1.put("PageChild", PageChild);

		json1.put("WTestParameter_ID", "2");
		json1.put("Name", "二级水平衡");
		PageChild=	WTestList("2",TheBeginDate,TheEndDate);
		json1.put("PageChild", PageChild);
		jsonArray.add(json1);
	
		json1.put("WTestParameter_ID", "3");
		json1.put("Name", "三级水平衡");
		PageChild=	WTestList("3",TheBeginDate,TheEndDate);
		json1.put("PageChild", PageChild);
		jsonArray.add(json1);
	
		json1.put("WTestParameter_ID", "4");
		json1.put("Name", "四级水平衡");
		PageChild=	WTestList("4",TheBeginDate,TheEndDate);
		json1.put("PageChild", PageChild);
		jsonArray.add(json1);
		
		return jsonArray.toString();
	}
	
	//2 水平衡测试数据
	private	JSONObject WTestList(String WTestParameter_ID,String TheBeginDate,String TheEndDate)
	{
//select convert(varchar(16),TestTime,120)TestTime,InGross,OutGross,LoseGross  from WTestRecord

		JSONObject jsonArray = new JSONObject();
		
		JSONArray jsonFlowX = new JSONArray();
		JSONArray InGross = new JSONArray();//进水
		JSONArray OutGross = new JSONArray();//出水
		JSONArray LoseGross = new JSONArray();//漏损
		JSONArray LoseRa = new JSONArray();//漏损率
		jsonFlowX.add( "2018-05-01");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		
		
		jsonFlowX.add( "2018-05-02");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);

		jsonFlowX.add( "2018-05-03");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-04");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-05");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-06");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-07");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);

		jsonFlowX.add( "2018-05-08");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-09");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-10");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-11");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-12");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-13");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonFlowX.add( "2018-05-14");
		InGross.add( 116);
		OutGross.add( 116);
		LoseGross.add( 20);
		LoseRa.add( 20);
		jsonArray.put("InGross",InGross);
		jsonArray.put("OutGross",OutGross);
		jsonArray.put("LoseGross",LoseGross);
		jsonArray.put("LoseRa",LoseRa);
		jsonArray.put("jsonFlowX",jsonFlowX);
		
		return jsonArray;
	}

	
}
