package com.xiaojie.service.impl;

import com.xiaojie.dao.GoodsMapper;
import com.xiaojie.pojo.GoodsVo;
import com.xiaojie.pojo.OrderInfo;
import com.xiaojie.pojo.User;
import com.xiaojie.service.GoodsService;
import com.xiaojie.service.OrderService;
import com.xiaojie.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: SeckillProject
 * @description: 秒杀服务
 * @author: Mr.Li
 * @create: 2020-05-31 11:40
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    /**
     * 秒杀业务：减库存下订单，写入订单
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        //减库存
        goodsService.reduceStock(goods);

        //创建订单:order_info  seckill_order
        return orderService.createOrder(user,goods);
    }
}
