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
    private static CacheMap instance = new CacheMap();
    public static CacheMap getInstance() {
        if (instance == null) {
            instance = new CacheMap();
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
