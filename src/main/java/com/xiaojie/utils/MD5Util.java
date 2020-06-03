package com.xiaojie.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @program: SeckillProject
 * @description: md5加密工具类
 * @author: Mr.Li
 * @create: 2020-05-30 14:56
 **/
public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "9d5b364d";

    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String saltDB) {
        //MD5密码生成：salt：9d5b364d
        String formPass = inputPassToFormPass(inputPass);
        //数据库密码生成：salt：1l2j3g
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        //3457ceaeba3426466887369f1a1f7a88
        System.out.println(inputPassToFormPass("123456"));
    }
}
