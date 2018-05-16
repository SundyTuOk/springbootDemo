package com.tu.manager;

import com.tu.manager.config.dbconfig.DS1Config;
import com.tu.manager.config.dbconfig.DS2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@ComponentScan(value = "com.tu")
@EnableAutoConfiguration
@SpringBootApplication
//@EnableJpaRepositories(value = {"com.tu.manager.dao","com.tu.manager.dao2"}) 有毒啊，这句不能要，暂时不知道为甚
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
