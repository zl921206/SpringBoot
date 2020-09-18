package com.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * @author zhanglei
 * SpringBoot项目启动成功之后，如果需要做一些额外的操作，可以在本类实现
 * 实现CommandLineRunner接口，则在springBoot项目启动成功之后，执行其实现方法run()
 */
@EnableScheduling
@Component
@Slf4j
public class StartupConfig implements CommandLineRunner {

    /**
     * 注入服务应用名称
     */
    @Value("${spring.application.name}")
    String applicationName;
    /**
     * 注入服务需要加载的环境配置，如：local,dev,test,prod
     */
    @Value("${spring.profiles.active}")
    String projectEnv;

    /**
     * SpringBoot项目启动成功之后，执行该方法
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>>>>>> 服务["+applicationName+"]，环境["+projectEnv+"]启动已经完成 <<<<<<<<<<<<< ");
    }
}
