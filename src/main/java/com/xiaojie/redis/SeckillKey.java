package com.xiaojie.redis;

/**
 * @program: SeckillProject
 * @description: 秒杀状态 key
 * @author: Mr.Li
 * @create: 2020-06-03 11:52
 **/
public class SeckillKey extends BasePrefix {
    private SeckillKey(String prefix) {
        super(prefix);
    }
    public static SeckillKey isGoodsOver = new SeckillKey("go");
    public static SeckillKey getSeckillPath = new SeckillKey("sp");
}
