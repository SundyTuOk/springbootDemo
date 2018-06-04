package com.tu.modules.login.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {

    /**
     * 圈子首页，登录页面
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletResponse rsp) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        // 没有登陆
        if (subject == null || !subject.isAuthenticated()) {
            return "login/login";
        }
        return "redirect:index/index.html";
    }

    /**
     * 认证
     * @return
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest request) throws Exception{
        // shiro在认证过程中出现错误后将异常类路径通过request返回
        String exceptionClassName = (String) request
                .getAttribute("shiroLoginFailure");
        // 登陆有错误，继续跳转登陆页面
        if(exceptionClassName != null){
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

            return "login/login";
        }

        Subject subject = SecurityUtils.getSubject();
        // 没有登陆
        if (subject == null || !subject.isAuthenticated()) {
            return "login/login";
        }

        return "redirect:index/index.html";
    }

}
