package com.tu.manager.controller;

import com.tu.manager.annotation.DataSource;
import com.tu.manager.dao.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@EnableJpaRepositories("com.tu.manager.dao")
//@EntityScan("com.tu.manager.entity")
@RestController
public class Index1 {

    @RequestMapping(value="/hello",method = RequestMethod.GET)
    public String hello(){
        return "hello springboot";
    }

    @Autowired
    private AdminDao adminDao;


    @RequestMapping(value="/jpa1",method = RequestMethod.GET)
    @DataSource(name = "dataSource1")
    public String jpa1(){
        Map<String, String> stringStringMap = adminDao.find();
        return stringStringMap.get("realname");
    }

    @RequestMapping(value="/jpa2",method = RequestMethod.GET)
    @DataSource(name = "dataSource2")
    public String jpa2(){
        Map<String, String> stringStringMap = adminDao.find();
        return stringStringMap.get("realname");
    }

}
