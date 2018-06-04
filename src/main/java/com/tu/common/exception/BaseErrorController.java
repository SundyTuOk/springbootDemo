package com.tu.common.exception;

import net.sf.json.JSONObject;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "error")
public class BaseErrorController implements ErrorController {

    private final String NOT_FOUND_PAGE = "error/404";
    private final String INTERNAL_SERVER_ERROR_PAGE = "error/500";

    @Override
    public String getErrorPath() {
        return NOT_FOUND_PAGE;
    }

    @RequestMapping
    public Object error(HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        System.out.println(remoteUser);

        HttpStatus httpStatus = this.getStatus(request);
        if (httpStatus == HttpStatus.NOT_FOUND){
            if (isAjax(request)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status", 404);
                jsonObject.put("msg", "页面没有找到");
                return jsonObject.toString();
            }
            return NOT_FOUND_PAGE;
        }
        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            if (isAjax(request)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status", 500);
                jsonObject.put("msg", "服务器内部错误");
                return jsonObject.toString();
            }
            return INTERNAL_SERVER_ERROR_PAGE;
        }
        return getErrorPath();
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    /**
     * 判断请求是否为ajax请求
     */
    public static boolean isAjax(HttpServletRequest request){
        return(request.getHeader("X-Requested-With") != null
                &&"XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }

}
