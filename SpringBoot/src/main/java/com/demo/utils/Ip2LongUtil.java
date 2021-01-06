package com.demo.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * IP地址与Long进行相互转换工具类
 */
public class Ip2LongUtil {

    //Long转换为IP
    private static String numberToIp(Long number) {
        //等价上面
        String ip = "";
        for (int i = 3; i >= 0; i--) {
            ip += String.valueOf((number & 0xff));
            if (i != 0) {
                ip += ".";
            }
            number = number >> 8;
        }

        return ip;
    }

    //IP转换为Long
    public static long ipToLong(String ip) {
        String[] ipArray = ip.split("\\.");
        List ipNums = new ArrayList();
        for (int i = 0; i < 4; ++i) {
            ipNums.add(Long.valueOf(Long.parseLong(ipArray[i].trim())));
        }
        long ZhongIPNumTotal = ((Long) ipNums.get(0)).longValue() * 256L * 256L * 256L
                + ((Long) ipNums.get(1)).longValue() * 256L * 256L + ((Long) ipNums.get(2)).longValue() * 256L
                + ((Long) ipNums.get(3)).longValue();

        return ZhongIPNumTotal;
    }

    public static void main(String[] args) {
        String ipaddr = "119.29.13.228";
        long l = ipToLong(ipaddr);
        System.out.println(l);
    }

}
