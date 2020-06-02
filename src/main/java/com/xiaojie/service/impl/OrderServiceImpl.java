package com.xiaojie.service.impl;

import com.xiaojie.dao.OrderMapper;
import com.xiaojie.pojo.GoodsVo;
import com.xiaojie.pojo.OrderInfo;
import com.xiaojie.pojo.SeckillOrder;
import com.xiaojie.pojo.User;
import com.xiaojie.redis.OrderKey;
import com.xiaojie.redis.RedisService;
import com.xiaojie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: SeckillProject
 * @description: 订单服务类
 * @author: Mr.Li
 * @create: 2020-05-31 11:30
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 根据用户id和商品id查询订单，从redis缓存获取
     * @param userId
     * @param goodsId
     * @return
     */
    public SeckillOrder getOrderByUserIdGoodsId(int userId, long goodsId) {
        //先访问缓存
        SeckillOrder seckillOrder =  redisService.get(OrderKey.getOrderByUidOid,""+userId + "_"+goodsId,SeckillOrder.class);
        if(seckillOrder == null) {
            return orderMapper.getOrderByUserIdGoodsId(userId,goodsId);
        }
        return seckillOrder;
    }

    /**
     * 生成订单
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        //orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId((long)user.getId());
        long orderId = orderMapper.insert(orderInfo);

        //seckillOrder
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId((long)user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);

        //把订单放入缓存
        redisService.set(OrderKey.getOrderByUidOid,""+user.getId() + "_"+goods.getId(),seckillOrder,3600);
        return orderInfo;
    }

    /**
     * 根据订单id获取订单
     * @param orderId
     * @return
     */
    public OrderInfo getByOrderId(long orderId) {
        return orderMapper.getOrderById(orderId);
    }
}
