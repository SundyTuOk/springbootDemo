package com.sf.controller.Err_List;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.utils.Constant;

@Controller
@RequestMapping("/Err_List")
public class Err_List {

	//1 区块异常信息列表
	@RequestMapping(value="/GetErrList",method=RequestMethod.GET) 
		@ResponseBody
		public String GetErrList(HttpServletRequest req) throws IOException{
		//显示当天的即可
		//select Err_ID,Area_ID,Err_Type,'管网漏损' TypeName,Err_Title,Err_Time  from ErrList  
		//Err_Type:1:管道漏损,2,区块水平衡异常
			String Area_ID = req.getParameter("Area_ID");
			String Type = req.getParameter("Type");//0：查询所有，其他值筛选ErrList中的Err_Type
			String StartTime = req.getParameter("StartTime");//起始时间
			String EndTime = req.getParameter("EndTime");//终止时间
			String sortName = req.getParameter("sortName");//终止时间
			String sortType = req.getParameter("sortType");//终止时间
			String SelectCon = req.getParameter("SelectCon");//终止时间
	
		
			//Type为空就当0处理
			//ID,Num,Name
			JSONArray jsonArray = new JSONArray();
			JSONObject json1 = new JSONObject();
			json1.put("Err_ID", "4");
			json1.put("Area_ID", "1");
			json1.put("Area_Name", "华中师范大学");
			json1.put("Err_Type", "1");
			json1.put("TypeName", "管道漏损");
			json1.put("Err_Title", "2018-05-14 管道漏损，进水200，出水100，漏损率：50%");
			json1.put("Err_Time", "2018-05-14 10:20");
			
			jsonArray.add(json1);
			JSONObject json2 = new JSONObject();
			json2.put("Err_ID", "5");
			json2.put("Area_ID", "1");
			json1.put("Area_Name", "华中师范大学");
			json2.put("Err_Type", "2");
			json2.put("TypeName", "区块水平衡异常");
			json2.put("Err_Title", "2018-05-14 区块水平衡异常，进水200，出水100，漏损率：50%");
			json2.put("Err_Time", "2018-05-14 10:20");
			
			jsonArray.add(json2);
			JSONObject jsonR = new JSONObject();
			jsonR.put("totalCount", 3);
			jsonR.put("rows", jsonArray);
			return jsonR.toString();
		}

		

	//2 删除列表
	@RequestMapping(value="/DelErrList",method=RequestMethod.GET) 
		@ResponseBody
		public String DelErrList(HttpServletRequest req) throws IOException{
		//显示当天的即可
		//select Err_ID,Area_ID,Err_Type,'管网漏损' TypeName,Err_Title,Err_Time  from ErrList  
		//Err_Type:1:管道漏损,2,区块水平衡异常
			String List = req.getParameter("List");

			JSONObject retJson=new  JSONObject();
			//按照约定方式返回
			retJson.put(Constant.RESULT, Constant.SUCCESS);
			retJson.put(Constant.EFFECT_COUNT, 1);
			return retJson.toString();
		}
}
