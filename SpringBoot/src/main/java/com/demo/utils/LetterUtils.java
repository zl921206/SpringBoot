package com.demo.utils;

/**
 * 字母转换工具类
 */
public class LetterUtils {

    /**
     * 首字母转小写
     *
     * @param str
     * @return
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0)))
            return str;
        else
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }


    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0)))
            return str;
        else
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
    }
}
