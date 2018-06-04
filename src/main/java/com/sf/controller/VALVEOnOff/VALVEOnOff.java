package com.sf.controller.VALVEOnOff;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.scripting.xmltags.VarDeclSqlNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.dao.MeterDAO;
import com.sf.dao.NetWorkDAO;

@Controller
@RequestMapping("/VALVEOnOff")
public class VALVEOnOff {
	@Resource(name = "meterDAO")
	private MeterDAO meterDAO;
	@Resource(name = "netWorkDAO")
	private NetWorkDAO netWorkDAO;
	
	
	private	JSONArray jsonNetWorkList ;
	
	//1 开关阀
		@RequestMapping(value="/VALVEListOnOff",method=RequestMethod.GET) 
		@ResponseBody
		public String VALVEListOnOff(HttpServletRequest req) throws IOException{
			//OnOff, VALVEID
			String VALVEList = req.getParameter("VALVEList");//[{"VALVEID":119,"OnOff":"On"}]
			JSONArray	JVALVEList=	JSONArray.fromObject(VALVEList);//要操作的阀门
			
		
			List<Map<String,Object>> AllNetWorkList=	netWorkDAO.getAllNetWorkList();
			List<Map<String,Object>> getAllPNetWork=	netWorkDAO.getAllPNetWork();
			//组装管网模型
			//要操作的回路
			 jsonNetWorkList =LaodNetWork(AllNetWorkList,getAllPNetWork);
			
			
			
			for(int i=0;i<JVALVEList.size();i++){
				JSONObject jsonV = JVALVEList.getJSONObject(i);
				//查找阀门所在的管道
				String VALVEID=jsonV.getString("VALVEID");
				String OnOff=jsonV.getString("OnOff");
				List<Map<String,Object>> NetWorkList=	meterDAO.getVALUENetWork(VALVEID);
				for (Map<String, Object> NetWorkInfo : NetWorkList) {
					//
					String  MeterStyle= NetWorkInfo.get("MeterStyle").toString();
					/*if(MeterStyle.equals("1"))//进水
					{*/
						//查询管道
						Integer index =	GetNetWork(NetWorkInfo.get("NetWork_ID").toString());
						if(index<0)
							continue;
						
						JSONObject netWork=(JSONObject) jsonNetWorkList.get(index);
						
						CNetWorkOnOff( OnOff,  netWork, index) ;
						
					/*}
					else if(MeterStyle.equals("2"))//中间
					{
						
					}*/
				}
			}
			/*查询阀门所在管道及下级管道
			1.如果阀门所在的管道上配置信息为“入水表”，所在管道及下方所属管网同时级联操作
			2.如果阀门所在的管道上配置信息为“中间表”或者“出水表”，所在管道不变更状态，下方所属管网同时级联操作
			3.关阀时，每确定一个管道有无水状态时，先要确认该管道的入水管道信息，只有两个入水管道都没有水改管道才确定没有水，
			NetWork_ID,NetWork_Num,NetWork_Name
			*/

			
		/*	JSONArray jsonArray = new JSONArray();
			JSONObject json1 = new JSONObject();
			json1.put("NetWork_ID", "1");
			json1.put("NetWork_Num", "0016");
			json1.put("NetWork_Name", "国交泵房总出水");
			json1.put("NetWork_OnOff", "Off");
			jsonArray.add(json1);*/

		
			
			
			return jsonNetWorkList.toString();
		}
		//管网级联修改
		private void CNetWorkOnOff(String OnOff, JSONObject netWork,int index) {
		
			//处理上级管道
			JSONArray PList=(JSONArray)	netWork.get("PList");
			
			boolean isAllOff=true;//父节点是否全部为关阀，只有父节点全部关阀，本段管网才是真的没有水
			if(PList.size()>0)
			{
			
					for(int i=0;i<PList.size();i++){
						JSONObject	netWorkP=PList.getJSONObject(i);
						String PNetWork_ID=netWorkP.getString("NetWork_ID");
						//检查上级节点的状态
						int Pindex= GetNetWork( PNetWork_ID);
						if(Pindex>=0)
						{
							JSONObject	 PNetWork=jsonNetWorkList.getJSONObject(Pindex);
							String 	PNetWork_OnOff=PNetWork.getString("NetWork_OnOff");
							if(PNetWork_OnOff.equals("On"))
							{
								isAllOff=false;
							}
							netWorkP.put("NetWork_OnOff", PNetWork_OnOff);
							
						}
					}
				
			}
			
			String NetWork_ID=netWork.getString("NetWork_ID");
		
			if(OnOff.equals("Off") && isAllOff)
			{
				OnOff="Off";
			}
			else {
				OnOff="On";
			}
			netWork.put("NetWork_OnOff", OnOff);
			//jsonNetWorkList.set(index, netWork);
			JSONArray CList=(JSONArray)	netWork.get("CList");
			if(CList.size()>0)
			{
				for(int i=0;i<CList.size();i++){
					JSONObject	netWorkC=CList.getJSONObject(i);
					String CNetWork_ID=netWorkC.getString("NetWork_ID");
					netWorkC.put("NetWork_OnOff", OnOff);
					//CList.set(i, netWorkC);
					
					//递归向下
					Integer Cindex =	GetNetWork(CNetWork_ID);
					if(Cindex<0)
						continue;
					
					JSONObject CnetWork=(JSONObject) jsonNetWorkList.get(Cindex);
					 CNetWorkOnOff( OnOff,  CnetWork, Cindex);
					 
				}
	
			}
		}
		
		//根据ID查询管网信息
		private int GetNetWork(String NetWork_ID) {
			JSONObject netWork=null;//=new JSONObject();
			for(int i=0;i<jsonNetWorkList.size();i++){
			if(	jsonNetWorkList.getJSONObject(i).getString("NetWork_ID").equals(NetWork_ID))
			{
				netWork=jsonNetWorkList.getJSONObject(i);
				return i;
			}
			}
			return -1;
		}
		//系统中所有的管网
		//系统中所有的管网关系
		private JSONArray LaodNetWork(List<Map<String,Object>> AllNetWorkList,List<Map<String,Object>> getAllPNetWork ) {
			//select NetWork_ID,NetWork_Num,NetWork_Name from NetWork
			//select NetWork_ID,PNetWork_ID from PNetWork 
			JSONArray NetWorkList = new JSONArray();
			for (Map<String, Object> NetWorkInfo : AllNetWorkList) {
				JSONObject jsonNetWorkInfo = new JSONObject();
				String NetWork_ID=NetWorkInfo.get("NetWork_ID").toString();
				jsonNetWorkInfo.put("NetWork_ID", NetWorkInfo.get("NetWork_ID").toString());
				jsonNetWorkInfo.put("NetWork_Num", NetWorkInfo.get("NetWork_Num").toString());
				jsonNetWorkInfo.put("NetWork_Name", NetWorkInfo.get("NetWork_Name").toString());
				jsonNetWorkInfo.put("NetWork_OnOff", "On");
				JSONArray PList = new JSONArray();//父节点
				for (Map<String, Object> LInfo : getAllPNetWork) {
					if(LInfo.get("NetWork_ID").toString().equals(NetWork_ID))
					{
						JSONObject PInfo = new JSONObject();
						PInfo.put("NetWork_ID", LInfo.get("PNetWork_ID").toString());
						PInfo.put("NetWork_OnOff", "On");
						PList.add(PInfo);
					}
				}
				
				JSONArray CList = new JSONArray();//子节点
				for (Map<String, Object> LInfo : getAllPNetWork) {
					if(LInfo.get("PNetWork_ID").toString().equals(NetWork_ID))
					{
						JSONObject PInfo = new JSONObject();
						PInfo.put("NetWork_ID", LInfo.get("NetWork_ID").toString());
						PInfo.put("NetWork_OnOff", "On");
						CList.add(PInfo);
					}
				}
				
				
				jsonNetWorkInfo.put("PList", PList);
				jsonNetWorkInfo.put("CList", CList);
				
				
				NetWorkList.add(jsonNetWorkInfo);
			}
			
			return NetWorkList;
		}
}
