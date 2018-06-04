package com.sf.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class Task {
	@Scheduled(cron="0/5 * *  * * ? ")   //每10秒执行一次      
    public void aTask(){      
         DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
         System.out.println(sdf.format(new Date())+"*********A任务每5秒执行一次进入测试");      
    }      
}
