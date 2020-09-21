package com.demo.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局缓存
 */
public class CacheMap {

    /**
     * 定义一个线程安全的map
     */
    public static final Map<String, Object> currentHashMap = new ConcurrentHashMap<>();

    private Object lock = new Object();

    private CacheMap() {}

    /**
     * 定义饿汉式单例
     */
//    private static CacheMap instance = new CacheMap();
//    public static CacheMap getInstance() {
//        if (instance == null) {
//            instance = new CacheMap();
//        }
//        return instance;
//    }

    /**
     * 定义线程安全的懒汉式单例
     * 多线程时使用volatile关键字: 有两个作用：1：保持线程可见性；2：指令重排序
     */
    private static volatile CacheMap instance = null;
    public static CacheMap getInstance() {
        // 先判断对象是否为空（作用：降低服务开销，防止多线程环境，所有线程都直接进行同步等待锁，造成资源占用过大）
        if (instance == null) {
            //同步锁: 锁当前对象
            synchronized(CacheMap.class){
                // 再判断一次对象是否为空（保证该instanceLazy实例仅被初始化一次）
                if(instance == null){
                    instance = new CacheMap();
                }
            }
        }
        return instance;
    }

    /**
     * 添加一个map值
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        synchronized (lock) {
            currentHashMap.put(key, value);
        }
    }

    /**
     * 获取一个map值
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return currentHashMap.get(key);
    }

    /**
     * 删除一个map值
     * @param key
     */
    public void remove(String key) {
        currentHashMap.remove(key);
    }

}
