package com.tu.manager.config.dbconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//@Configuration
public class DataSourceBean {

//    @Bean(name = "firstDataSource")
////    @Qualifier("firstDataSource")
//    @ConfigurationProperties(prefix = "spring.first.datasource")
//    public DataSource firstDataSource() {
//        return DataSourceBuilder.create().build();
//    }

//    @Bean(name = "secondaryDataSource")
////    @Qualifier("secondaryDataSource")
//    @ConfigurationProperties(prefix = "spring.secondary.datasource")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
}
