package com.tu.manager.controller;

import com.tu.manager.annotation.DataSource;
import com.tu.manager.dao.AdminDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

//@EnableJpaRepositories("com.tu.manager.dao")
//@EntityScan("com.tu.manager.entity")
@Controller
public class Index1 {

    @RequestMapping(value="/hello",method = RequestMethod.GET)
    public String hello(){
        return "hello springboot";
    }

    @Autowired
    private AdminDao adminDao;


    @RequestMapping(value="/jpa1",method = RequestMethod.GET)
    @DataSource(name = "dataSource1")
    @ResponseBody
    public String jpa1(Map<String,Object> map,int length){
        System.out.println(length);
        Map<String, String> stringStringMap = adminDao.find();
        map.put("test",stringStringMap.get("realname"));
        JSONObject json = new JSONObject();
        json.put("test",stringStringMap.get("realname"));
        return json.toString();
    }

    @RequestMapping(value="/jpa2",method = RequestMethod.GET)
    @DataSource(name = "dataSource2")
    public String jpa2(Map<String,Object> map){
        Map<String, String> stringStringMap = adminDao.find();
        map.put("test",stringStringMap.get("realname"));
        return "index";
    }

}
