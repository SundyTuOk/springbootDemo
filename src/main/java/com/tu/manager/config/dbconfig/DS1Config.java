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
        entityManagerFactoryRef = "entityManagerFactory1",//配置连接工厂 entityManagerFactory
        transactionManagerRef = "transactionManager1", //配置 事物管理器  transactionManager
        basePackages = {"com.tu.manager.dao"})//设置dao（repo）所在位置
public class DS1Config {

    @Bean(name = "firstDataSource")
//    @Qualifier("firstDataSource")
    @ConfigurationProperties(prefix = "spring.first.datasource")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    @Qualifier("firstDataSource")
    private DataSource primaryDataSource;



    @Primary
    @Bean(name = "entityManagerPrimary1")
    public EntityManager entityManager() {
        return entityManagerFactory().getObject().createEntityManager();
    }



//    @Primary
//    @Bean(name = "entityManagerFactoryPrimary")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
//        return entityManagerFactoryBuilder.dataSource(primaryDataSource).properties(getVendorProperties(primaryDataSource)).packages("com.zh.SpringBootDemo.domain.p") // 设置实体类所在位置
//                .persistenceUnit("primaryPersistenceUnit").build();
//    }

    @Primary
    @Bean(name = "entityManagerFactory1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource);
        em.setPackagesToScan("com.tu.manager.entity");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        em.setJpaProperties(properties);

        return em;
    }



//    @Autowired
//    private JpaProperties jpaProperties;



    private Map<String, String> getVendorProperties(DataSource dataSource) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("show-sql","true");
//        return jpaProperties.getHibernateProperties(dataSource);
        return  map;
    }



    @Primary
    @Bean(name = "transactionManager1")
    public PlatformTransactionManager transactionManagerPrimary() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }
}
