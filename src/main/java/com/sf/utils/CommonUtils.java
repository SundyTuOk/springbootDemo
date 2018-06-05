package com.sf.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {
	
	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	/**
     * 生成32位md5码
     * @param password
     * @return
     */
    public static String md5Password(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
        	logger.error(e+"\nMD5加密失败");
            return null;
        }

    }
    
    /**
     * 本程序规定程序中所有cookie对象从这里取，方便统一管理设置
     * @return
     */
    public static Cookie getNewCookie(String cookieName,String cookieValue){
    	Cookie cookie = new Cookie(cookieName, cookieValue);
    	cookie.setMaxAge(60 * 60 * 24 * 10);//10天
    	return cookie;
    }
    
    /**
     * <p>add</p>
     * @param a
     * @param b
     * @return
     */
    public static float add(float a, float b) {
        
        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.add(b2).floatValue();
        
        return f;
        
    }
    
    /**
     * <p>subtract</p>
     * @param a
     * @param b
     * @return
     */
    public static float subtract(float a, float b) {
        
        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.subtract(b2).floatValue();
        
        return f;
        
    }
    
    /**
     * <p>multiply</p>
     * @param a
     * @param b
     * @return
     */
    public static float multiply(float a, float b) {
        
        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.multiply(b2).floatValue();
        
        return f;
        
    }
    
    /**
     * <p>divide</p>
     * <p>当不整除，出现无限循环小数时，向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6</p>
     * @param a
     * @param b
     * @return
     */
    public static float divide(float a, float b) {
        
        return divide(a, b, 2, BigDecimal.ROUND_HALF_UP);
        
    }
    
    /**
     * <p>divide</p>
     * @param a
     * @param b
     * @param scale
     * @param roundingMode
     * @return
     */
    public static float divide(float a, float b, int scale, int roundingMode) {
        
        /*
         * 通过BigDecimal的divide方法进行除法时就会抛异常的，异常如下：
         * java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result. at java.math.BigDecimal.divide(Unknown Source)
         * 解决之道：就是给divide设置精确的小数点divide(xxxxx,2, BigDecimal.ROUND_HALF_EVEN)
         * BigDecimal.ROUND_HALF_UP : 向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
         */
        
        BigDecimal b1 = new BigDecimal(a + "");
        BigDecimal b2 = new BigDecimal(b + "");
        float f = b1.divide(b2, scale, roundingMode).floatValue();
        
        return f;
        
    }
    
    /**
     * 返回接口失败json
     * @return
     */
    public static JSONObject getFailJson(){
    	JSONObject json = new JSONObject();
    	json.put(Constant.RESULT, Constant.FAILED);
    	json.put(Constant.EFFECT_COUNT, 0);
    	return json;
    }
    
    /**
     * 返回接口成功json
     * @return
     */
    public static JSONObject getSuccessJson(){
    	JSONObject json = new JSONObject();
    	json.put(Constant.RESULT, Constant.SUCCESS);
    	json.put(Constant.EFFECT_COUNT, 1);
    	return json;
    }
    
    /**
     * 根据传入开始日期和结束日期，得到表名的后缀名数组
     * @param start
     * @param end
     * @return
     */
    public static String[] getTableNameSuffix(Date start,Date end){
    	Calendar startCalendar = Calendar.getInstance();
    	Calendar endCalendar = Calendar.getInstance();
    	startCalendar.setTime(start);
    	endCalendar.setTime(end);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    	List<String> tableNameSuffixList = new ArrayList<String>();
    	while(!startCalendar.after(endCalendar)){
    		String tableNameSuffix = sdf.format(startCalendar.getTime());
    		tableNameSuffixList.add(tableNameSuffix);
    		// 加一个月
    		startCalendar.add(Calendar.MONTH, 1);
    	}
    	String[] tableNameSuffixArr = new String[tableNameSuffixList.size()];
    	tableNameSuffixList.toArray(tableNameSuffixArr);
    	return tableNameSuffixArr;
    }
    
}
