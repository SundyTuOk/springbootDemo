package com.tu.common.exception;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

        @ExceptionHandler(value = Exception.class)
        public Object jsonHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {

//            ModelAndView mv = new ModelAndView();
//            mv.setViewName("/error/500.html");
            if (isAjax(request)){
                JSONObject json = new JSONObject();
                json.put("msg", "你错了");
                return json.toString();
            }
            ModelAndView mv = new ModelAndView();
            mv.setViewName("error/500.html");
//            response.sendRedirect("error/500.html");
            return mv;
        }

        private void log(Exception ex, HttpServletRequest request) {
            logger.error("************************异常开始*******************************");
//        if(getUser() != null)
//            logger.error("当前用户id是" + getUser().getUserId());
            logger.error(ex);
            logger.error("请求地址：" + request.getRequestURL());
            Enumeration enumeration = request.getParameterNames();
            logger.error("请求参数");
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement().toString();
                logger.error(name + "---" + request.getParameter(name));
            }

            StackTraceElement[] error = ex.getStackTrace();
            for (StackTraceElement stackTraceElement : error) {
                logger.error(stackTraceElement.toString());
            }
            logger.error("************************异常结束*******************************");
        }

    /**
     * 判断请求是否为ajax请求
     */
    public static boolean isAjax(HttpServletRequest request){
        return(request.getHeader("X-Requested-With")!=null
                &&"XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }
}
