package com.tu.manager.aspect;

import com.tu.manager.annotation.DataSource;
import com.tu.manager.config.dbconfig.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class DataSourceAspect implements Ordered {

    @Pointcut("@annotation(com.tu.manager.annotation.DataSource)")
    public void dataSourcePointCut(){}

    @Around("dataSourcePointCut()")
    public Object setDataSource(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DataSource ds = method.getAnnotation(DataSource.class);
        if (ds == null) {
            DynamicDataSource.setDataSource("dataSource1");
            System.out.println("set datasource is " + "FIRST");
        } else {
            DynamicDataSource.setDataSource(ds.name());
            System.out.println("set datasource is " + ds.name());
        }
        try {
            return pjp.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            System.out.println("clean datasource");
        }
    }


    @Override
    public int getOrder() {
        return 1;
    }
}
