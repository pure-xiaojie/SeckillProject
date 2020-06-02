package com.xiaojie.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: SeckillProject
 * @description: 秒杀订单数据封装
 * @author: Mr.Li
 * @create: 2020-06-02 11:19
 **/
@Getter
@Setter
public class OrderDetail {
    private GoodsVo goods;
    private OrderInfo order;
}
