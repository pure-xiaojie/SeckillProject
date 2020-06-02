package com.xiaojie.service;

import com.xiaojie.pojo.GoodsVo;
import com.xiaojie.pojo.OrderInfo;
import com.xiaojie.pojo.SeckillOrder;
import com.xiaojie.pojo.User;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    /**
     * 根据用户id和商品id查询订单
     * @param userId
     * @param goodsId
     * @return
     */
    public SeckillOrder getOrderByUserIdGoodsId(int userId, long goodsId);

    /**
     * 生成订单
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods);

    /**
     * 根据订单id获取订单
     * @param orderId
     * @return
     */
    public OrderInfo getByOrderId(long orderId);
}
