package com.demo.service;

/**
 * @author zhanglei
 * @package com.demo.service
 * @description 异步任务服务，配合线程池使用
 * @since 2020-09-18
 *
 * 使用方法：在其他bean中注入该服务接口，调用execute方法即可，但在多线程调用方法之上必须要添加注解 @Async("asyncExecutor")
 * 注意：asyncExecutor: 对应线程池配置类ExecutorConfig中的bean名称
 * 调用方式：
 *  1：无返回值的方法添加注解直接调用
 *  2：如果多线程需要执行的方法有返回值，则必须使用返回类型Future<T>定义，并返回new AsyncResult<>(T)；
 */
public interface AsyncTaskService {
    /**
     * 执行异步任务
     */
    void execute(String value);
}
