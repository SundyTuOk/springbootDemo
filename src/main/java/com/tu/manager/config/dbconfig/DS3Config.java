package com.tu.manager.config.dbconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "entityManagerFactoryPrimary",//配置连接工厂 entityManagerFactory
//        transactionManagerRef = "transactionManagerPrimary", //配置 事物管理器  transactionManager
//        basePackages = {"com.tu.manager.dao"})//设置dao（repo）所在位置
public class DS3Config {

//    @Autowired
//    private JpaProperties jpaProperties;

//    @Autowired
//    @Qualifier("secondaryDataSource")
//    private DataSource secondaryDS;
//
//    @Bean(name = "entityManagerSecondary")
//    public EntityManager entityManager(EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
//        return entityManagerFactorySecondary(
//                entityManagerFactoryBuilder).getObject().createEntityManager();
//    }
//
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
//
//    private Map<String, String> getVendorProperties(DataSource dataSource) {
//        Map<String,String> map = new HashMap<String,String>();
//        map.put("show-sql","true");
////        return jpaProperties.getHibernateProperties(dataSource);
//        return  map;
//    }
//
//    @Bean(name = "transactionManagerSecondary")
//    PlatformTransactionManager transactionManagerSecondary(
//            EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
//        return new JpaTransactionManager(
//                entityManagerFactorySecondary(entityManagerFactoryBuilder).getObject());
//    }
}
