package com.tu.manager.config.dbconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceBean {

//    @Bean(name = "dataSource1")
//    @ConfigurationProperties(prefix = "spring.first.datasource")
//    public DataSource dataSource1() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "dataSource2")
//    @ConfigurationProperties(prefix = "spring.secondary.datasource")
//    public DataSource dataSource2() {
//        return DataSourceBuilder.create().build();
//    }

}
