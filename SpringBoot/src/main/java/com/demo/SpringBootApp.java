package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Spring Boot 启动类
 * @author Administrator
 *
 */
@SpringBootApplication
// 开启过滤器扫描
@ServletComponentScan
@Slf4j
public class SpringBootApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApp.class, args);
		log.info("SpringBootApp start success ......");
	}
}
