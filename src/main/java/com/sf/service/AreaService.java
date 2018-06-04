package com.sf.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sf.bean.Area;
import com.sf.dao.AreaDAO;

@Service("areaService")
@Lazy(value = true)
public class AreaService {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private AreaDAO areaDAO;
	
	public List<Area> getAllAreas(){
		return areaDAO.getAllAreas();
	}
	
	/**
	 * 递归实现区块父子关系
	 * @param areas
	 * @param id
	 * @return
	 */
	public List<Object> getChild(List<Area> areas,Integer id){
		List<Object> rootMap = new ArrayList<Object>();
		Iterator<Area> iterator = areas.iterator();
		while(iterator.hasNext()){
			Area area = iterator.next();
			if(area.getSuperiorAreaID() == id.intValue()){
				Map<Object,Object> map = new LinkedHashMap<Object,Object>();
				map.put("pNodeId", area.getSuperiorAreaID());
				map.put("cNodeId", area.getAreaID());
				map.put("text", area.getAreaName());
				map.put("nodeValue", area.getAreaNum());
				map.put("nodes", getChild(areas,area.getAreaID()));
				rootMap.add(map);
			}
		}
		return  rootMap;
	}
	
	public Area getAreaByAreaNum(String areaNum){
		return areaDAO.getAreaByAreaNum(areaNum);
	}
	
	public boolean updateArea(Area area){
		int effectCount = areaDAO.updateArea(area);
		if(effectCount == 0){
			return false;
		}
		return true;
	}
	
	public boolean addArea(Area area){
		int effectCount = areaDAO.addArea(area);
		if(effectCount == 0){
			return false;
		}
		return true;
	}

	public boolean deleteArea(String areaNum) {
		int effectCount = areaDAO.deleteArea(areaNum);
		if(effectCount == 0){
			return false;
		}
		return true;
	}
	
	public Area getAreaByAreaID(int areaID){
		return areaDAO.getAreaByAreaID(areaID);
	}
	//获取区块用水表计用于计算区块总用水量
	public String AreaGrossWAMeter() throws IOException{
			/*List<Area> areas = areaService.getAllAreas();
			List<Object> testMap = areaService.getChild(areas,0);
			JSONArray fromObject = JSONArray.fromObject(testMap);
			logger.info("fromObject->"+fromObject.toString());*/
			//先查询区块是否有测试模型
		
			return "";//fromObject.toString();
		}
}
