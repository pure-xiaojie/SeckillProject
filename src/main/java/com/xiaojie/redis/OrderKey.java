package com.xiaojie.redis;

/**
 * @program: SeckillProject
 * @description: 订单key
 * @author: Mr.Li
 * @create: 2020-06-02 11:48
 **/
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    //获取订单键值
    public static OrderKey getOrderByUidOid = new OrderKey("ok");
}
