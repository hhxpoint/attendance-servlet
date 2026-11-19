package com.attendance.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * @author 梁宏博
 */
public class MD5Util {
    
    /**
     * 对字符串进行MD5加密
     * @param source 原始字符串
     * @return 加密后的32位小写字符串
     */
    public static String md5(String source) {
        if (source == null || source.isEmpty()) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
    
    /**
     * 验证密码是否匹配
     * @param input 输入的明文密码
     * @param stored 存储的MD5密文
     * @return 是否匹配
     */
    public static boolean verify(String input, String stored) {
        return md5(input).equals(stored);
    }
}
