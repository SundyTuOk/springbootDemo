package com.tu.manager.config.dbconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
import java.util.Properties;

@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "entityManagerFactory1",//配置连接工厂 entityManagerFactory
//        transactionManagerRef = "transactionManager1", //配置 事物管理器  transactionManager
//        basePackages = {"com.tu.manager.dao"})//设置dao（repo）
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory1",//配置连接工厂 entityManagerFactory
        transactionManagerRef = "transactionManager1")//设置dao（repo）所在位置
public class DS1Config {

    @Bean(name = "dataSource1")
    @ConfigurationProperties(prefix = "spring.first.datasource")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    @Qualifier("dataSource1")
    private DataSource dataSource1;



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
        em.setDataSource(dataSource1);
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

    @Primary
    @Bean(name = "transactionManager1")
    public PlatformTransactionManager transactionManagerPrimary() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }
}
