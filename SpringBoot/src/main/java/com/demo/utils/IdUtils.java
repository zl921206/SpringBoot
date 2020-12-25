package com.demo.utils;

/**
 * ID生成器工具类
 * 
 * @author zhanglei
 */
public class IdUtils {
    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     * 
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUIDUtils.fastUUID().toString();
    }

}
