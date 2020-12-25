package com.demo.utils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * MD5加密工具类
 */
public class MD5Utils {

	private static final String saltValue = "";

    public static String encode(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 带盐值加密
     *
     * @param str  待加密字符串
     * @param salt 盐值
     */
    public static String encode(String str, String salt) {

        return encode(str + salt);
    }

    /**
     * 获取盐值（随机字符串）
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

	public static void main(String[] args) {
		String salt = getRandomString(20);
		System.out.println("salt：" + salt);
		System.out.println(encode("a123456" + salt));
	}

}
