package com.tu.manager.config.dbconfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = MyBatisDataSourceConfig.MASTER_PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MyBatisDataSourceConfig {
    //扫描从数据源mapper接口所在的包
    static final String MASTER_PACKAGE = "com.tu.manager.dao";
    //扫描从数据源xml文件
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

//    @Value("${master.datasource.url}")
//    private String url;
//
//    @Value("${master.datasource.username}")
//    private String user;
//
//    @Value("${master.datasource.password}")
//    private String password;
//
//    @Value("${master.datasource.driverClassName}")
//    private String driverClass;


    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MyBatisDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
