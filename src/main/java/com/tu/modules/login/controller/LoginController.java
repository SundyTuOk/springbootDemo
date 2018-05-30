package com.tu.modules.login.controller;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    /**
     * 圈子首页，登录页面
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "login";
    }

    /**
     * 认证
     * @return
     */
    @RequestMapping("/loginUrl")
    public String login(HttpServletRequest request) throws Exception{
        // shiro在认证过程中出现错误后将异常类路径通过request返回
        String exceptionClassName = (String) request
                .getAttribute("shiroLoginFailure");
        if(exceptionClassName!=null){
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                throw new Exception("账号不存在");
            } else if (IncorrectCredentialsException.class.getName().equals(
                    exceptionClassName)) {
                throw new Exception("用户名/密码错误");
            } else if("randomCodeError".equals(exceptionClassName)){
                throw new Exception("验证码错误");
            } else{
//                throw new Exception();//最终在异常处理器生成未知错误
            }
        }
        return "login";
    }

}
