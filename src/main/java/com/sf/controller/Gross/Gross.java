package com.sf.controller.Gross;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sf.bean.NetWork;
import com.sf.bean.ZWA;
import com.sf.dao.MeterDAO;
import com.sf.dao.NetWorkDAO;
import com.sf.dao.WTestDAO;
import com.sf.utils.CommonUtils;
import com.sun.org.apache.xerces.internal.impl.dv.xs.DecimalDV;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
@Lazy(value = true)
public class Gross {

	@Autowired
	private WTestDAO wTestDAO;
	@Autowired
	private MeterDAO meterDAO;
	@Autowired
	private NetWorkDAO netWorkDAO;
	
	//10
	//水表时间段内小时用水
	public	JSONArray WAHourGross(String Meter_ID,String StartTime,String EndTime) throws ParseException
	{
		List<Map<String,Object>> VES_AmmeterHourData=WAHourGrossData(Meter_ID, StartTime, EndTime);
		//时间为 TheDate前一天0点到TheDate当天23点
		JSONArray jsonArray = new JSONArray();
		//起始时间至终止时间内的流量
		//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
		JSONArray jsonFlow = new JSONArray();
		JSONArray jsonFlowX = new JSONArray();
		for (Map<String, Object> mdateMap : VES_AmmeterHourData) {
		
			jsonFlow.add(  mdateMap.get("AmmeterData"));
			jsonFlowX.add( mdateMap.get("ValueTime"));
		}
		jsonArray.add(jsonFlowX);
		jsonArray.add(jsonFlow);
		
		return jsonArray;

	}
	
	//10
	//水表时间段内小时用水
	public	List<Map<String,Object>>  WAHourGrossData(String Meter_ID,String StartTime,String EndTime) throws ParseException
	{
		Calendar calendar = Calendar.getInstance();
	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //加上时间
	Date dateS=dateFormat.parse(StartTime);
	Date dateE=dateFormat.parse(EndTime);
	calendar.setTime(dateS);
	calendar.add(Calendar.HOUR, -1);
	Date dateSData=calendar.getTime();//起始数据要取前一个小时的
		String [] MonthArry=CommonUtils.getTableNameSuffix(dateSData,dateE) ; //GetTableMonth(dateFormat.format(dateSData),EndTime);//获取数据存储的分表信息
		
		List<Map<String,Object>> VES_AmmeterHourData=new ArrayList<Map<String,Object>>();

		List<Map<String,Object>> liz=	meterDAO.getWAMeterDataH(Meter_ID, dateFormat.format(dateSData),  EndTime, MonthArry);

		//起始时间取头一天结算时间
		
		calendar.setTime(dateS);
		int ii=		calendar.get(Calendar.HOUR_OF_DAY) ;
		if( ii==0)
		{

			Double minDateValue=-1.00;
			
			List<Map<String,Object>> liS=	meterDAO.getT_DayAm(Meter_ID, StartTime);
			if(liS.size()>0)
			{
				minDateValue=Double.parseDouble( liS.get(0).get("EndZValueZY").toString())		;	
				if(minDateValue>0)
				{
					String minDateTime=liS.get(0).get("EndReadingDate").toString();
					boolean has=false;
					for (Map<String, Object> map : liz) {
						if( calendar.get(Calendar.YEAR) ==Integer.parseInt( map.get("D_Y").toString())&& calendar.get(Calendar.MONTH) + 1 ==Integer.parseInt( map.get("D_M").toString())&&calendar.get(Calendar.DAY_OF_MONTH) ==Integer.parseInt( map.get("D_D").toString())&&calendar.get(Calendar.HOUR_OF_DAY) ==Integer.parseInt( map.get("D_H").toString()))
						{
							has=true;
							map.put("value", minDateValue);
							
						}
					}
					
					if(!has)
					{
						
						Map<String,Object> mdateMap=new HashMap<String,Object>();
						mdateMap.put("D_Y",calendar.get(Calendar.YEAR));
						mdateMap.put("D_M", calendar.get(Calendar.MONTH) + 1);
						mdateMap.put("D_D", calendar.get(Calendar.DAY_OF_MONTH));
						mdateMap.put("D_H", calendar.get(Calendar.HOUR_OF_DAY));
						mdateMap.put("value", minDateValue);
						liz.add(mdateMap);
					}
				}
			}

		}
		
	float	 StarValue=-1F;
	float EndValue=-1F;
	float TheValue=0F;
	Date TheDate=dateS;
	Calendar cc = Calendar.getInstance();
	cc.setTime(dateE);
	cc.add(Calendar.HOUR, 1);
	Date endDate=cc.getTime();
	while(TheDate.compareTo(endDate)<=0)
	{
		calendar.setTime(TheDate);
		boolean has=false;
		for (Map<String, Object> map : liz) {
			if( calendar.get(Calendar.YEAR) ==Integer.parseInt( map.get("D_Y").toString())&& calendar.get(Calendar.MONTH) + 1 ==Integer.parseInt( map.get("D_M").toString())&&calendar.get(Calendar.DAY_OF_MONTH) ==Integer.parseInt( map.get("D_D").toString())&&calendar.get(Calendar.HOUR_OF_DAY) ==Integer.parseInt( map.get("D_H").toString()))
			{
				has=true;
				EndValue=Float.parseFloat(map.get("value").toString());
				if(StarValue==-1)
				{
						 TheValue=0F;
						 StarValue=EndValue;
						 has=false;
							break;
				}
					else
					{
						
						cc.setTime(TheDate);
						cc.add(Calendar.HOUR, -1);
						
						if (cc.getTime().compareTo(dateS)>=0)//DATEADD(Hour,-1,@TheDate)>=@beginDate 
						{
						TheValue=CommonUtils.subtract(EndValue,StarValue);
						//insert into #VES_AmmeterHourData values(DATEADD(Hour,-1,@TheDate),@TheValue);
						Map<String,Object> mdateMap=new HashMap<String,Object>();
						mdateMap.put("ValueTime",dateFormat.format( cc.getTime()));
						mdateMap.put("AmmeterData",String.valueOf( TheValue));
						VES_AmmeterHourData.add(mdateMap);
						}
						StarValue=EndValue;
					}
				break;
			}
		}
		if(!has)
		{	
		cc.setTime(TheDate);
		cc.add(Calendar.HOUR, -1);
			if (cc.getTime().compareTo(dateS)>=0)//DATEADD(Hour,-1,@TheDate)>=@beginDate 
					{
						TheValue=0F;
						//insert into #VES_AmmeterHourData values(DATEADD(Hour,-1,@TheDate),@TheValue);
						Map<String,Object>  mdateMap=new HashMap<String,Object>();
						mdateMap.put("ValueTime",dateFormat.format( cc.getTime()));
						if (cc.getTime().compareTo(new Date())>=0)//大于当前时间就设置空值
						{
							mdateMap.put("AmmeterData", 0);
						}
						else
						mdateMap.put("AmmeterData", "0.00");
						VES_AmmeterHourData.add(mdateMap);
					}
			
		}
	
	calendar.add(Calendar.HOUR, 1);
	TheDate=calendar.getTime();
	}
	
	

		return VES_AmmeterHourData;

	}
	
	//区块计算用量的表计列表
	public	ArrayList<String> GetAreaGrossMeterList(String Area_ID) throws ParseException
	{
		ArrayList<String> meterListIn=new ArrayList<String>();//区块总用量表计
		ArrayList<String> meterListOut=new ArrayList<String>();//区块总用量表计
		//区块的默认模型ID
	String DefaultWTestParameter_ID=	wTestDAO.getAreasDefaultWTestParameter_ID(1);
		if(!DefaultWTestParameter_ID.isEmpty())
		{
		
			//获取区块的进水表、和出水表
			List<Map<String,Object>> lim=	wTestDAO.getWTestMeter(DefaultWTestParameter_ID);
			for (Map<String, Object> map : lim) {
				if( map.get("MeterStyle").toString().equals("0"))
				{
					meterListIn .add( map.get("Meter_ID").toString());
				}
				else {
					meterListOut .add( map.get("Meter_ID").toString());
				}
			}
			
		}
		if(meterListIn.size()>0)
			return meterListIn;
		else {
			return meterListOut;
		}
	}
	//区块时间段内小时用水
	public	JSONArray AreaHourGross(String Area_ID,String StartTime,String EndTime) throws ParseException
	{
		if (!StartTime.contains(" "))
			StartTime+=" 00:00";
		if (!EndTime.contains(" "))
			EndTime+=" 00:00";
		
		ArrayList<String> meterList=GetAreaGrossMeterList(Area_ID);
	if(meterList.size()==0)
		meterList.add("0");
	
	List<Map<String,Object>> VES_AmmeterHourData=new ArrayList<Map<String,Object>>();
	
	for (String Meter_ID : meterList) {
		//单表用量
		List<Map<String,Object>> AmmeterHourData=WAHourGrossData(Meter_ID, StartTime, EndTime);
		
		for ( Integer i=0;i<AmmeterHourData.size();i++) {
			Map<String, Object> map = AmmeterHourData.get(i);
			if(VES_AmmeterHourData.size()<=i)//总数里面没有 
			{
				VES_AmmeterHourData.add(map);
				continue;
			}
			else {
				Map<String, Object> zmap = VES_AmmeterHourData.get(i);
				String sssString=zmap.get("ValueTime").toString();
				if(map.get("AmmeterData")==null && zmap.get("AmmeterData")==null )
				{
					continue;
				}
				else {
					float ZGross=0F;
					float Gross=0F;
					if(zmap.get("AmmeterData")!=null )
					{
						ZGross=Float.parseFloat(zmap.get("AmmeterData").toString());
					}
					if(map.get("AmmeterData")!=null )
					{
						Gross=Float.parseFloat(map.get("AmmeterData").toString());
					}
					ZGross=CommonUtils.add(ZGross,Gross);
					
					zmap.put("AmmeterData", String.valueOf( ZGross));
				}
			}
			
	
		}
	}

			//时间为 TheDate前一天0点到TheDate当天23点
			JSONArray jsonArray = new JSONArray();
			//起始时间至终止时间内的流量
			//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
			JSONArray jsonFlow = new JSONArray();
			JSONArray jsonFlowX = new JSONArray();
			for (Map<String, Object> mdateMap : VES_AmmeterHourData) {
				jsonFlowX.add( mdateMap.get("ValueTime"));
				jsonFlow.add(  mdateMap.get("AmmeterData"));
			}

			jsonArray.add(jsonFlow);
			jsonArray.add(jsonFlowX);
			return jsonArray;

	}
		
	public	JSONObject AreaDayGross(String Area_ID,String StartTime,String EndTime) throws ParseException
	{
		//时间为 TheDate前一天0点到TheDate当天23点
		JSONArray jsonArray = new JSONArray();
		//起始时间至终止时间内的流量
		//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
		JSONArray jsonFlowIn = new JSONArray();
		JSONArray jsonFlowOut = new JSONArray();
		JSONArray jsonFlowMin = new JSONArray();
		JSONArray jsonFlowX = new JSONArray();
		List<Map<String,Object>> liz=wTestDAO.getAreaDayGross(Area_ID,StartTime,EndTime);
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //加上时间
		SimpleDateFormat shotF=new SimpleDateFormat("yyyy-MM-dd"); //加上时间
		if (!StartTime.contains(" "))
			StartTime+=" 00:00";
		if (!EndTime.contains(" "))
			EndTime+=" 00:00";
		Date dateS=dateFormat.parse(StartTime);
		Date dateE=dateFormat.parse(EndTime);
		
		
		while(dateS.compareTo(dateE)<=0)
		{calendar.setTime(dateS);
			boolean has=false;
			for (Map<String, Object> map : liz) {
				if( calendar.get(Calendar.YEAR) ==Integer.parseInt( map.get("SelectYear").toString())&& calendar.get(Calendar.MONTH) + 1 ==Integer.parseInt( map.get("SelectMonth").toString())&&calendar.get(Calendar.DAY_OF_MONTH) ==Integer.parseInt( map.get("SelectDay").toString()))
				{
					jsonFlowX.add(map.get("SelectYear").toString()+"-"+map.get("SelectMonth").toString()+"-"+map.get("SelectDay").toString());
					jsonFlowIn.add(String.valueOf(map.get("ZGrossIn").toString()));
					jsonFlowOut.add(String.valueOf(map.get("ZGrossOut").toString()));
					jsonFlowMin.add(String.valueOf(map.get("ZGrossMin").toString()));
					has=true;
					break;
				}
				
			}
			if(!has)
			{
				jsonFlowX.add(shotF.format(dateS));
				jsonFlowIn.add(0);
				jsonFlowOut.add(0);
				jsonFlowMin.add(0);
			}
			
			calendar.add(Calendar.DATE, 1);
			dateS=calendar.getTime();//
		}
		
		
		
		
		
		
JSONObject json1 = new JSONObject();
json1.put("In", jsonFlowIn);
json1.put("Out", jsonFlowOut);
json1.put("Min", jsonFlowMin);
json1.put("X", jsonFlowX);

		
		return json1;
	}

	public	JSONObject AreaMonthGross(String Area_ID,String StartTime,String EndTime) throws ParseException
	{
		//时间为 TheDate前一天0点到TheDate当天23点
		JSONArray jsonArray = new JSONArray();
		//起始时间至终止时间内的流量
		//select ValueTime,ZvalueZY  from Z_FL_201804 order by ValueTime
		JSONArray jsonFlowIn = new JSONArray();
		JSONArray jsonFlowOut = new JSONArray();
		JSONArray jsonFlowMin = new JSONArray();
		JSONArray jsonFlowX = new JSONArray();
		if (!StartTime.contains(" "))
			StartTime+=" 00:00";
		if (!EndTime.contains(" "))
			EndTime+=" 00:00";
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //加上时间
		SimpleDateFormat shotF=new SimpleDateFormat("yyyy-MM"); //加上时间
		Date dateS=dateFormat.parse(StartTime);
		Date dateE=dateFormat.parse(EndTime);
		
		
		List<Map<String,Object>> liz=wTestDAO.getAreaMonthGross(Area_ID,StartTime,EndTime);
		
		
		
		while(dateS.compareTo(dateE)<=0)
		{calendar.setTime(dateS);
			boolean has=false;
			for (Map<String, Object> map : liz) {
				if( calendar.get(Calendar.YEAR) ==Integer.parseInt( map.get("SelectYear").toString())&& calendar.get(Calendar.MONTH) + 1 ==Integer.parseInt( map.get("SelectMonth").toString()))
				{
					jsonFlowX.add(map.get("SelectYear").toString()+"-"+map.get("SelectMonth").toString());
					jsonFlowIn.add(String.valueOf(map.get("ZGrossIn").toString()));
					jsonFlowOut.add(String.valueOf(map.get("ZGrossOut").toString()));
					jsonFlowMin.add(String.valueOf(map.get("ZGrossMin").toString()));
					has=true;
					break;
				}
				
			}
			if(!has)
			{
				jsonFlowX.add(shotF.format(dateS));
				jsonFlowIn.add(0);
				jsonFlowOut.add(0);
				jsonFlowMin.add(0);
			}
			
			calendar.add(Calendar.MONTH, 1);
			dateS=calendar.getTime();//
		}
		
		
		
		JSONObject json1 = new JSONObject();
		json1.put("In", jsonFlowIn);
		json1.put("Out", jsonFlowOut);
		json1.put("Min", jsonFlowMin);
		json1.put("X", jsonFlowX);

				
				return json1;
	}
}
