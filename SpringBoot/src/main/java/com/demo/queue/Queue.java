package com.demo.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.AbstractQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @desc 定义队列
 */
@Slf4j
@Component
public class Queue<T> {

    /**
     * 定义阻塞队列
     */
    private static BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(10);

    /**
     * 定义基于链接节点的、线程安全的队列
     */
    private static AbstractQueue absQueue = new ConcurrentLinkedQueue();

    /**
     * 存放队列
     *
     * @param t
     * @throws InterruptedException
     */
    public void produce(T t) throws InterruptedException {
        queue.put(t);
    }

    /**
     * 队列取出
     *
     * @return
     * @throws InterruptedException
     */
    public T consume() throws InterruptedException {
        return (T)queue.take();
    }

}
