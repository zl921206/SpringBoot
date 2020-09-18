package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhanglei
 * @package com.demo.config
 * @description 线程池配置
 * @since 2020-09-18
 */
@Configuration
@EnableAsync
public class ExecutorConfig implements AsyncConfigurer {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(20);
        //配置最大线程数
        executor.setMaxPoolSize(50);
        //配置队列大小
        executor.setQueueCapacity(400);
        //配置线程池中的线程名称前缀
        executor.setThreadNamePrefix("ThreadPool-");
        //policy: 当pool已经达到Max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
