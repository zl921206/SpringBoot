package com.demo.constant;

/**
 * 常量类
 */
public class Constant {

    /**
     * redis-用户token存储
     */
    public static final String USER_TOKEN = "user_token";

    /**
     * redis-用户userKey存储
     */
    public static final String USER_KEY = "user_key";

    /**
     * redis-用户信息存储
     */
    public static final String USER_INFO = "user_info";

    /**
     * @author zhanglei
     * 冒号：拼接在redis key值之前，可以将冒号之前的字符串作为redis文件夹使用
     */
    public static final String COLON = ":";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 点号
     */
    public static final String DOT = ".";

    /**
     * 空
     */
    public static final String EMPTY = "";

    /**
     * userKey值常量属性
     */
    public static final String USER_KEY_ATTR = "userKey";

    /**
     * token值常量属性
     */
    public static final String TOKEN_ATTR = "token";
}