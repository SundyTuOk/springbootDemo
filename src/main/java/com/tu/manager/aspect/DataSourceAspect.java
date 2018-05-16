package com.tu.manager.aspect;

import com.tu.manager.Utils.SpringUtils;
import com.tu.manager.annotation.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Configuration
public class DataSourceAspect {

    @Resource(name = "entityManagerFactory1")
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
    @Autowired
    private javax.sql.DataSource dataSource1;
    @Autowired
    private javax.sql.DataSource dataSource2;


    @Pointcut("@annotation(com.tu.manager.annotation.DataSource)")
    public void dataSourcePointCut(){}

    @Around("dataSourcePointCut()")
    public Object setDataSource(ProceedingJoinPoint pjp) throws Throwable {
//        LocalContainerEntityManagerFactoryBean entityManagerFactory =
//                (LocalContainerEntityManagerFactoryBean) springUtils.getBean("entityManagerFactory1");
        javax.sql.DataSource dataSource = entityManagerFactory.getDataSource();

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DataSource dataSourceAnnotation = method.getAnnotation(DataSource.class);
        if (dataSourceAnnotation == null || dataSourceAnnotation.name() == null){
            Object retObject = pjp.proceed();
            return retObject;
        }
//        javax.sql.DataSource tempDataSource =
//                (javax.sql.DataSource) springUtils.getBean(dataSourceAnnotation.name());
//        if (tempDataSource == null){
//            Object retObject = pjp.proceed();
//            return retObject;
//        }
        System.out.println("查询前更改数据源:"+dataSourceAnnotation.name());
        if (dataSourceAnnotation.name().equals("dataSource1")){
            entityManagerFactory.setDataSource(dataSource1);
        }
        if (dataSourceAnnotation.name().equals("dataSource2")){
            entityManagerFactory.setDataSource(dataSource2);
        }

        Object retObject = pjp.proceed();
        entityManagerFactory.setDataSource(dataSource);
        System.out.println("查询后更改数据源:"+dataSource.toString());

        return retObject;

    }



}
