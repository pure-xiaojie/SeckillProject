package com.xiaojie.redis;

/**
 * @program: SeckillProject
 * @description: 键值抽象类
 * @author: Mr.Li
 * @create: 2020-05-30 14:29
 **/
public class BasePrefix implements KeyPrefix {
    private String prefix;

    public BasePrefix( String prefix) {
        this.prefix = prefix;
    }

    /**
     * 键生成
     * @return
     */
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }
}
