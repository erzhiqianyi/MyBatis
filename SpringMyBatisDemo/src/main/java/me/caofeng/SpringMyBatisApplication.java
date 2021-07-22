package me.caofeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages ="me.caofeng.mapper")
public class SpringMyBatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMyBatisApplication.class, args);
	}

}
