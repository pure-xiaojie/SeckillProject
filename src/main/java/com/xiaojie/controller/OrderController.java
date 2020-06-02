package com.xiaojie.controller;

import com.xiaojie.pojo.GoodsVo;
import com.xiaojie.pojo.OrderDetail;
import com.xiaojie.pojo.OrderInfo;
import com.xiaojie.pojo.User;
import com.xiaojie.result.CodeMsg;
import com.xiaojie.result.Result;
import com.xiaojie.service.GoodsService;
import com.xiaojie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: SeckillProject
 * @description: 订单表现层
 * @author: Mr.Li
 * @create: 2020-06-02 11:18
 **/
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetail> info(User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getByOrderId(orderId);
        if(orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        long goodsId = orderInfo.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(orderInfo);
        orderDetail.setGoods(goods);
        return Result.success(orderDetail);
    }
}
