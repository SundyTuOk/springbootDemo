package com.sf.controller.systeminfo;

import com.sf.bean.Area;
import com.sf.commonbase.Transaction;
import com.sf.service.AreaService;
import com.sf.utils.Constant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/area")
public class AreaControllor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name="areaService")
	private AreaService areaService;
	
	@Resource(name="transaction")
	private Transaction txManager;
	/**
	 * 系统信息，区块信息管理
	 * @throws IOException 
	 */
	@RequestMapping(value="/areaInfo",method=RequestMethod.POST) 
	@ResponseBody
	public String areaManage() throws IOException{
		List<Area> areas = areaService.getAllAreas();
		List<Object> testMap = areaService.getChild(areas,0);
		JSONArray fromObject = JSONArray.fromObject(testMap);
		logger.info("fromObject->"+fromObject.toString());
		
		return fromObject.toString();
	}
	
	@RequestMapping(value="/addArea",method=RequestMethod.POST) 
	@ResponseBody
	public String addArea(HttpServletRequest req){
		JSONObject retJson = new JSONObject();
		int effectCount = 0;
		// 这个地方areaID，如果传过来0，代表新增区块，如果传过来1，代表编辑区块的areaID
		String areaID = req.getParameter("areaID");
		String areaName = req.getParameter("areaName");
		String areaNum = req.getParameter("areaNum");
		String DLModeFileAddress = req.getParameter("DLModeFileAddress");
		String JGModeFileAddress = req.getParameter("JGModeFileAddress");
		String SuperiorAreaID = req.getParameter("SuperiorAreaID");
		
		if(areaNum == null){
			logger.error("addArea传入参数异常areaNum->"+areaNum);
			retJson.put(Constant.RESULT, Constant.FAILED);
			retJson.put(Constant.EFFECT_COUNT, effectCount);
			return retJson.toString();
		}
		
		int areaIDInt = 0;
		Area area = new Area();
		try{
			if(SuperiorAreaID != null){
				int superiorAreaIDInt = Integer.parseInt(SuperiorAreaID);
				area.setSuperiorAreaID(superiorAreaIDInt);
			}
			if(areaID != null){
				areaIDInt = Integer.parseInt(areaID);
			}
		}catch(NumberFormatException e){
			logger.error(e.toString());
			logger.error("addArea传入错误SuperiorAreaID->"+SuperiorAreaID);
			retJson.put(Constant.RESULT, Constant.FAILED);
			retJson.put(Constant.EFFECT_COUNT, effectCount);
			return retJson.toString();
		}
		area.setAreaNum(areaNum);
		area.setAreaName(areaName);
		area.setDLModeFileAddress(DLModeFileAddress);
		area.setJGModeFileAddress(JGModeFileAddress);
		
//		Area areaByAreaNum = areaService.getAreaByAreaNum(areaNum);
		if(areaIDInt != 0){
			// 本身存在这个area，现在执行的是更新操作，而不是添加
			area.setAreaID(areaIDInt);
			if(area.getSuperiorAreaID() != null){
				//这个还要求更新superID父表ID,那么也就是要更新areaList字段
				Area superArea = areaService.getAreaByAreaID(area.getSuperiorAreaID());
				area.setAreaList(superArea.getAreaList() +","+ area.getAreaID());
			}
			boolean status = areaService.updateArea(area);
			if(status){
				retJson.put(Constant.RESULT, Constant.SUCCESS);
				retJson.put(Constant.EFFECT_COUNT, 1);
				return retJson.toString();
			}
			retJson.put(Constant.RESULT, Constant.FAILED);
			retJson.put(Constant.EFFECT_COUNT, 0);
			return retJson.toString();
		}
		
		// 不存在这个area，现在执行的是添加操作，而不是更新
		boolean addStatus = false;
		boolean updateStatus = false;
		try{
			addStatus = areaService.addArea(area);
			// 更新插入Area的AreaList
			Area superArea = areaService.getAreaByAreaID(area.getSuperiorAreaID());
			Area tempArea = new Area();
			tempArea.setAreaID(area.getAreaID());
			tempArea.setAreaList(superArea.getAreaList() +","+ area.getAreaID());
			updateStatus = areaService.updateArea(tempArea);
		}catch(Exception e){
			logger.error(e.toString());
			logger.error("添加区块失败");
			retJson.put(Constant.RESULT, Constant.FAILED);
			retJson.put(Constant.EFFECT_COUNT, 0);
			return retJson.toString();
		}
		
		if(addStatus && updateStatus){
			retJson.put(Constant.RESULT, Constant.SUCCESS);
			retJson.put(Constant.EFFECT_COUNT, 1);
			return retJson.toString();
		}
		retJson.put(Constant.RESULT, Constant.FAILED);
		retJson.put(Constant.EFFECT_COUNT, 0);
		return retJson.toString();
	}
	
	@RequestMapping(value="/deleteArea",method=RequestMethod.POST)
	@ResponseBody
	public String deleteAreas(HttpServletRequest req){
		JSONObject retJson = new JSONObject();
		
		String areaNum = req.getParameter("areaNum");
		TransactionStatus status = txManager.beginTansaction();
		try{
			boolean result = areaService.deleteArea(areaNum);
			txManager.commit(status);
			if(result){
				retJson.put(Constant.RESULT, Constant.SUCCESS);
				retJson.put(Constant.EFFECT_COUNT, 1);
				return retJson.toString();
			}
		}catch(Exception e){
			logger.error(e.toString());
			logger.error("删除areaNum->"+areaNum+"失败");
			txManager.rollback(status);
			retJson.put(Constant.RESULT, Constant.FAILED);
			retJson.put(Constant.EFFECT_COUNT, 0);
			return retJson.toString();
		}
		
		logger.info("删除area失败,数据库不存在areaNum->"+areaNum);
		retJson.put(Constant.RESULT, Constant.FAILED);
		retJson.put(Constant.EFFECT_COUNT, 0);
		return retJson.toString();
	}
	
	
}
