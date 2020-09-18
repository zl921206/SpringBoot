package com.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {

    public static String htmlToText(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("").replaceAll("(\\\\r\\\\n|\\\\n|\\\\r|\\r\\n|\\n|\\r)", "").replaceAll("&nbsp;","").replaceAll("&rdquo;","").replaceAll("&bull;","").replaceAll("&ldquo;",""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    public static boolean isNotEmpty(String str){
        if((str != null) && (str.length() > 0) && !"null".equals(str) && !"".equals(str)){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object obj){
        if (obj == null || null == obj || "null".equals(obj)) {
            return true;
        }
        return false;
    }

}
