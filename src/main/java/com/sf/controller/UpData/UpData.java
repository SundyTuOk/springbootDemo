package com.sf.controller.UpData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.Buffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.dao.ZDAO;
import com.sf.utils.CommonUtils;
import com.sun.istack.internal.logging.Logger;

@Controller
@RequestMapping("/UpData")
public class UpData {

	@Autowired
	private ZDAO zDAO;
	
	@RequestMapping(value="/GetAllMeterInfoForSend",method=RequestMethod.POST) 
	@ResponseBody
	public String GetAllMeterInfoForSend(HttpServletRequest req) throws IOException{
		ServletInputStream inputStream = req.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
	    StringBuffer sb = new StringBuffer();
	    String temp = null;
	    while((temp = br.readLine()) != null){
	    	sb.append(temp);
	    }
	    br.close();
	    inputStream.close();
	    String rstrString = sb.toString();
	  //zDAO.  CreateDataTable();
	/*	rstrString=	"{";
		rstrString+=	"\"UpFrom\":\"S1\",";
		rstrString+=	"\"MeterData\":[{";
		rstrString+=	"\"Meter_Type\":\"WA\",";
		rstrString+=	"\"MeterList\":[{";
		rstrString+=	"\"Meter_Num\":3,";
		rstrString+=	"\"Meter_Name\": 3,";
		rstrString+=	"\"Meter_Changjia\": 3,";
		rstrString+=	"\"Meter_485Address\":3,";
		rstrString+=	"\"Beilv\":1,";
		rstrString+=	"\"Xiuzheng\":1,";
		rstrString+=	"\"UpID\":1";
		rstrString+=	"},{";
		rstrString+=	"\"Meter_Num\":2,";
		rstrString+=	"\"Meter_Name\":2,";
		rstrString+=	"\"Meter_Changjia\":2,";
		rstrString+=	"\"Meter_485Address\":2,";
		rstrString+=	"\"Beilv\":2,";
		rstrString+=	"\"Xiuzheng\":0,";
		rstrString+=	"\"UpID\":2";
		rstrString+=	"}]";
		rstrString+=	"},{";
		rstrString+=	"\"Meter_Type\":\"FR\",";
		rstrString+=	"\"MeterList\":[]";
		rstrString+=	"},{";
		rstrString+=	"\"Meter_Type\":\"FL\",";
		rstrString+=	"\"MeterList\":[]";
		rstrString+=	"}]}";*/
		
	  JSONObject	RObj=	JSONObject.fromObject(rstrString);//
	  System.out.println(RObj.toString());
	  JSONObject	SObj=	new JSONObject();
	 
	  if(RObj==null)
		  return "";
		String UpFrom=RObj.getString("UpFrom");
		 SObj.put("UpFrom", UpFrom);
		
	JSONArray	MeterDataList=RObj.getJSONArray("MeterData");
	JSONArray	MeterDataListS=new JSONArray();

	for (Object MO : MeterDataList) {
		JSONObject	MeterData=(JSONObject)MO;
		JSONObject	MeterDataS=new JSONObject();
		String Meter_Type=MeterData.getString("Meter_Type");
		MeterDataS.put("Meter_Type", Meter_Type);
		JSONArray	MeterList=MeterData.getJSONArray("MeterList");
		JSONArray	MeterListS=new JSONArray();
		for (Object MIO : MeterList) {
			JSONObject	MeterInfo=(JSONObject)MIO;
			JSONObject	MeterInfoS=new JSONObject();
				String	Meter_Num=MeterInfo.getString("Meter_Num");
				String	Meter_Name=MeterInfo.getString("Meter_Name");
				String	Meter_Changjia=MeterInfo.getString("Meter_Changjia");
				String	Meter_485Address=MeterInfo.getString("Meter_485Address");
				String	Beilv=MeterInfo.getString("Beilv");
				String	Xiuzheng=MeterInfo.getString("Xiuzheng");
				String	UpID=MeterInfo.getString("UpID");
				
				List<Map<String,Object>> zmapL=	zDAO.getMeterInfoForSend(UpFrom, UpID, Meter_Type);
				if(zmapL.size()>0)//数据库中已经有了该数据
				{
					Map<String, Object> zmap=zmapL.get(0);
					String Meter_ID=zmap.get("Meter_ID").toString();
					//检验是否要更新数据库
					if(!(Meter_Num.equals(zmap.get("Meter_Num").toString())
							&&Meter_Name.equals(zmap.get("Meter_Name").toString())
							&&Meter_Changjia.equals(zmap.get("Meter_Changjia").toString())
							&&Meter_485Address.equals(zmap.get("Meter_485Address").toString())
							&&Beilv.equals(zmap.get("Beilv").toString())
							&&Xiuzheng.equals(zmap.get("Xiuzheng").toString())))
					{
				
						zDAO.EditMeterInfoForSend(Meter_ID, Meter_Num, Meter_Name, Meter_Changjia, Meter_485Address, Beilv, Xiuzheng);
						
					}
					MeterInfoS.put("Meter_ID", zmap.get("Meter_ID").toString());
					MeterInfoS.put("Meter_Time", zmap.get("Meter_Time"));
					MeterInfoS.put("UpID", zmap.get("UpID").toString());
				}
				else {
					//数据库中没有该设备
					zDAO.AddMeterInfoForSend(Meter_Num, Meter_Name, Meter_Changjia, Meter_485Address, Beilv, Xiuzheng, Meter_Type, UpFrom, UpID);
					
					List<Map<String,Object>> amapL=	zDAO.getMeterInfoForSend(UpFrom, UpID, Meter_Type);
					if(amapL.size()>0)//数据库中已经有了该数据
					{
						Map<String, Object> amap=amapL.get(0);
						
						MeterInfoS.put("Meter_ID", amap.get("Meter_ID").toString());
						MeterInfoS.put("Meter_Time", amap.get("Meter_Time"));
						MeterInfoS.put("UpID", amap.get("UpID").toString());
					}
				}
				MeterListS.add(MeterInfoS);
		}
		
		MeterDataS.put("MeterList", MeterListS);
		MeterDataListS.add(MeterDataS);
	}
	
	SObj.put("MeterData", MeterDataListS);
	rstrString = SObj.toString();
	  
	/*	rstrString=	"{";
		rstrString+=	"\"UpFrom\":\"S1\",";
		rstrString+=	"\"MeterData\":[{";
		rstrString+=	"\"Meter_Type\":\"WA\",";
		rstrString+=	"\"MeterList\":[{";
		rstrString+=	"\"Meter_ID\": 1,";
		rstrString+=	"\"Meter_Time\":\"\",";
		rstrString+=	"\"UpID\":1";
		rstrString+=	"},{";
		rstrString+=	"\"Meter_ID\":1,";
		rstrString+=	"\"Meter_Time\":\"\",";
		rstrString+=	"\"UpID\": 1";
		rstrString+=	"}]";
		rstrString+=	"},{";
		rstrString+=	"\"Meter_Type\":\"FR\",";
		rstrString+=	"\"MeterList\":[]";
		rstrString+=	"},{";
		rstrString+=	"\"Meter_Type\":\"FL\",";
		rstrString+=	"\"MeterList\":[]";
		rstrString+=	"}]}";*/
		return rstrString;
	}
	
	private int count = 0;
	
	@RequestMapping(value="/GetAllMeterDatasForSend",method=RequestMethod.POST) 
	@ResponseBody
	public String GetAllMeterDatasForSend(HttpServletRequest req) throws IOException, ParseException{
		
		
		ServletInputStream inputStream = req.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
	    StringBuffer sb = new StringBuffer();
	    String temp = null;
	    while((temp = br.readLine()) != null){
	    	sb.append(temp);
	    }
	    br.close();
	    inputStream.close();
	    String rstrString=sb.toString();
		  rstrString = new String(rstrString.getBytes("gb2312"),"utf-8");
			
		  JSONObject	RObj=	JSONObject.fromObject(rstrString);//
		  if(RObj==null)
			  return "0";
		
		String UpFrom=RObj.getString("UpFrom");
		JSONArray	MeterDataList=RObj.getJSONArray("MeterData");
		String Meter_Type=RObj.getString("Meter_Type");
		String Meter_ID=RObj.getString("Meter_ID");
		String UpID=RObj.getString("UpID");
		
		String	ValueTime = null;
		String	ZValueZY = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = null;
		for (Object MO : MeterDataList) {
			JSONObject	MeterData=(JSONObject)MO;
	
			ValueTime=MeterData.getString("ValueTime");
			ZValueZY=MeterData.getString("ZValueZY");
			
			start = sdf.parse(ValueTime);
	
					
			zDAO.AddMeterDatasForSend(Meter_Type, Meter_ID, ValueTime,ZValueZY, UpFrom);
				
					
		}
		System.out.println(++count);
		
		return "1";

	}
	
	/**
	 * 涂召亮 修改上传程序
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/GetAllMeterDatasForSend1",method=RequestMethod.POST) 
	@ResponseBody
	public String GetAllMeterDatasForSend1(HttpServletRequest req) throws IOException, ParseException{
		
		
		ServletInputStream inputStream = req.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
	    StringBuffer sb = new StringBuffer();
	    String temp = null;
	    while((temp = br.readLine()) != null){
	    	sb.append(temp);
	    }
	    br.close();
	    inputStream.close();
	    String rstrString=sb.toString();
		  rstrString = new String(rstrString.getBytes("gb2312"),"utf-8");
			
		  JSONObject	RObj=	JSONObject.fromObject(rstrString);//
		  if(RObj==null)
			  return "0";
		
		String UpFrom=RObj.getString("UpFrom");
		JSONArray	MeterDataList=RObj.getJSONArray("MeterData");
		String Meter_Type=RObj.getString("Meter_Type");
		String Meter_ID=RObj.getString("Meter_ID");
		String UpID=RObj.getString("UpID");
		
		String	ValueTime = null;
		String	ZValueZY = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = null;
		for (Object MO : MeterDataList) {
			JSONObject	MeterData=(JSONObject)MO;
	
			ValueTime=MeterData.getString("ValueTime");
			ZValueZY=MeterData.getString("ZValueZY");
			
			start = sdf.parse(ValueTime);
	
					
			zDAO.AddMeterDatasForSend(Meter_Type, Meter_ID, ValueTime,ZValueZY, UpFrom);
				
					
		}
		System.out.println(++count);
		
		return "1";

	}
	 
	
}
