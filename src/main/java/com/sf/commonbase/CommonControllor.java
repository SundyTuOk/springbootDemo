package com.sf.commonbase;

/**
 * 本类提供一些公用的基本接口
 * @author tuzhaoliang
 * @date 2018年5月4日
 */
//@Controller
//@RequestMapping("/common")
public class CommonControllor {
	

//	@Resource(name="commonDAO")
//	private CommonDAO commonDAO;
//
//	@RequestMapping(value="/select",method=RequestMethod.POST)
//	@ResponseBody
//	public String selectTableByWhere(HttpServletRequest req,HttpServletResponse rsp){
//		String tableName = req.getParameter("tableName");
//		String searchCondition = req.getParameter("searchCondition");
//		logger.info(req.getRemoteAddr()+"请求查询表->"+tableName+",模糊查询->"+searchCondition);
//		List<Map<String, Object>> selectTableByWhere = null;
//		String search = "'%" + searchCondition + "%'";
//		String like = " like ";
//		String or = " or ";
//		StringBuffer sb = new StringBuffer();
//		try{
//			List<String> tableColumList = commonDAO.selectColumsByTableName(tableName);
//			for(String colunm : tableColumList){
//				sb.append(colunm).append(like).append(search).append(or);
//			}
//			sb = sb.delete(sb.length() - or.length(), sb.length());
//			selectTableByWhere = commonDAO.selectTableByWhere(tableName, sb.toString());
//		}catch(Exception e){
//			logger.error(e.toString());
//			logger.info("select * from "+tableName+" "+sb+" 失败，返回空详细信息json数据体");
//			JSONObject json = new JSONObject();
//			json.put(Constant.JSON_COUNT, 0);
//			JSONArray resultJsonArray = new JSONArray();
//			json.put(Constant.JSON_ROWS, resultJsonArray);
//			return json.toString();
//		}
//		JSONObject json = new JSONObject();
//		json.put(Constant.JSON_COUNT, selectTableByWhere.size());
//		JSONArray resultJsonArray = JSONArray.fromObject(selectTableByWhere);
//		json.put(Constant.JSON_ROWS, resultJsonArray);
//		return json.toString();
//	}
//
//	@Resource(name="meterService")
//	private MeterService meterService;
//
//	@RequestMapping(value="/test",method=RequestMethod.GET)
//	@ResponseBody
//	public void test(){
////		meterService.addMeter();
//	}
}
