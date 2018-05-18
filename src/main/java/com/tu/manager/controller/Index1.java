package com.tu.manager.controller;

import com.tu.manager.annotation.DataSource;
import com.tu.manager.dao.AdminDao;
import com.tu.manager.dao2.AdminDao2;
import com.tu.manager.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Autowired
    private AdminDao2 adminDao2;


    @RequestMapping(value="/jpa1",method = RequestMethod.GET)
    @DataSource(name = "dataSource1")
    public String jpa1(){
        List<Admin> all = adminDao.findAll();
        List<Admin> all2 = adminDao2.findAll();

        return all.get(0).getRealname() + "  " + all2.get(0).getRealname();
    }

    @RequestMapping(value="/jpa2",method = RequestMethod.GET)
    @DataSource(name = "dataSource2")
    public String jpa2(){
        List<Admin> all = adminDao.findAll();
        List<Admin> all2 = adminDao2.findAll();
        return all.get(0).getRealname() + "  " + all2.get(0).getRealname();
    }

}
