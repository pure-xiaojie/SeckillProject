package com.xiaojie.dao;

import com.xiaojie.pojo.OrderInfo;
import com.xiaojie.pojo.SeckillOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderMapper {
    /**
     * 根据用户与商品查询订单
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("select * from seckill_order where user_id=#{userId} and goods_id=#{goodsId} ")
    public SeckillOrder getOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    /**
     * 创建orderInfo,并返回创建好的id
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel, " +
            " status,create_date)values(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice}, " +
            " #{orderChannel},#{status},#{createDate}) ")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,
            before = false,statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    /**
     * 创建秒杀seckillOrder订单
     * @param seckillOrder
     */
    @Insert("insert into seckill_order(user_id,goods_id,order_id)values(#{userId},#{goodsId},#{orderId}) ")
    public void insertSeckillOrder(SeckillOrder seckillOrder);
}
