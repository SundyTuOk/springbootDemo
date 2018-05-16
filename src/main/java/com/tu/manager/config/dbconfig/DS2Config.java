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
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactorySecondary",//配置连接工厂 entityManagerFactory
        transactionManagerRef = "transactionManagerSecondary", //配置 事物管理器  transactionManager
        basePackages = {"com.tu.manager.dao2"})//设置dao（repo）所在位置
public class DS2Config {

//    @Autowired
//    private JpaProperties jpaProperties;
    @Bean(name = "secondaryDataSource")
    //    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.secondary.datasource")
    public DataSource secondaryDataSource() {
    return DataSourceBuilder.create().build();
}

    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource secondaryDS;

    @Bean(name = "entityManagerSecondary")
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


    @Bean(name = "entityManagerFactorySecondary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(secondaryDS);
        em.setPackagesToScan("com.tu.manager.entity");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        properties.setProperty("show-sql","true");
        em.setJpaProperties(properties);
        return em;
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("show-sql","true");
//        return jpaProperties.getHibernateProperties(dataSource);
        return  map;
    }

    @Bean(name = "transactionManagerSecondary")
    PlatformTransactionManager transactionManagerSecondary() {
        return new JpaTransactionManager(
                entityManagerFactory().getObject());
    }
}
