package com.xiaojie.redis;

/**
 * @program: SeckillProject
 * @description: 限流工具键值
 * @author: Mr.Li
 * @create: 2020-06-03 20:21
 **/
public class AccessKey extends BasePrefix {
    public AccessKey(String prefix) {
        super(prefix);
    }

    public static AccessKey withExpire() {
        return new AccessKey("access");
    }
}
