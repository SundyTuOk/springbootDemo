package com.sf.commonbase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 本类为每次处理请求的一个切口，每次所有的请求都会在这里进行预处理，已经后处理
 * @author tuzhaoliang
 * @date 2018年5月3日
 */
public class BaseInterceptor implements HandlerInterceptor{

	private Logger logger = Logger.getLogger(getClass());
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 拦截器设置运行跨域
		logger.debug("拦截器启动，预处理请求...");
		logger.debug("现在设置允许跨域访问");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		response.setCharacterEncoding("UTF-8");
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
