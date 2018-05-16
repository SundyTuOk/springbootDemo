package com.tu.manager.config.dbconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "entityManagerFactory2",//配置连接工厂 entityManagerFactory
//        transactionManagerRef = "transactionManager2", //配置 事物管理器  transactionManager
//        basePackages = {"com.tu.manager.dao2"})//设置dao（repo）所在位置
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory2",//配置连接工厂 entityManagerFactory
        transactionManagerRef = "transactionManager2")//设置dao（repo）所在位置
public class DS2Config {

    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "spring.secondary.datasource")
    public DataSource dataSource2() {
    return DataSourceBuilder.create().build();
}

    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;

    @Bean(name = "entityManager2")
    public EntityManager entityManager() {
        return entityManagerFactory().getObject().createEntityManager();
    }

//    @Bean(name = "entityManagerFactorySecondary")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary (
//            EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
////        LocalEntityManagerFactoryBean localEntityManagerFactoryBean = new LocalEntityManagerFactoryBean();
////        localEntityManagerFactoryBean.setPersistenceUnitName("secondaryPersistenceUnit");
////        return localEntityManagerFactoryBean;
//
//
//        return entityManagerFactoryBuilder
//                .dataSource(secondaryDS)
//                .properties(getVendorProperties(secondaryDS))
//                .packages("com.tu.manager.entity")
//                .persistenceUnit("secondaryPersistenceUnit")
//                .build();
//    }


    @Bean(name = "entityManagerFactory2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource2);
        em.setPackagesToScan("com.tu.manager.entity");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(getJpaProperties());
        return em;
    }

    private Properties getJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql","true");
        properties.setProperty("hibernate.format_sql","true");
        return  properties;
    }

    @Bean(name = "transactionManager2")
    PlatformTransactionManager transactionManagerSecondary() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }
}
