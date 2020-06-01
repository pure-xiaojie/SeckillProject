package com.xiaojie.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @program: SeckillProject
 * @description: redis 连接测试
 * @author: Mr.Li
 * @create: 2020-05-29 18:08
 **/
public class RedisTest {
    @Test
    public void connect() {
        Jedis jedis = new Jedis("114.116.255.239",6379,1000);
        System.out.println(jedis.ping());
    }
}
