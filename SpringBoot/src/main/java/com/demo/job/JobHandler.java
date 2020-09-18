package com.demo.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * xxlJob任务示例
 */
@Component
@Slf4j
public class JobHandler {

    /**
     * @XxlJob("jobHandler"): 定义jobHandler为当前类的执行器bean名称，交由xxlJob管理
     * @param param
     * @return
     */
    @XxlJob("jobHandler")
    public ReturnT<String> execute(String param) {
        XxlJobLogger.log("任务开始执行......");
        /**
         * 此处编写需要执行的业务代码
         */
        // 调用业务逻辑代码
        XxlJobLogger.log("任务结束执行......");
        return ReturnT.SUCCESS;
    }
}
