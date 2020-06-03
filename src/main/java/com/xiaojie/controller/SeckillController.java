package com.xiaojie.controller;

import com.xiaojie.pojo.GoodsVo;
import com.xiaojie.pojo.OrderInfo;
import com.xiaojie.pojo.SeckillOrder;
import com.xiaojie.pojo.User;
import com.xiaojie.rabbitmq.MQSender;
import com.xiaojie.rabbitmq.SeckillMessage;
import com.xiaojie.redis.GoodsKey;
import com.xiaojie.redis.RedisService;
import com.xiaojie.result.CodeMsg;
import com.xiaojie.result.Result;
import com.xiaojie.service.GoodsService;
import com.xiaojie.service.OrderService;
import com.xiaojie.service.SeckillService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: SeckillProject
 * @description: 秒杀表现层
 * @author: Mr.Li
 * @create: 2020-05-31 11:15
 **/
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    private Map<Long,Boolean> localOverMap = new HashMap<Long,Boolean>();
    /**
     * 1.0版
     * QPS:793
     * 线程：5000 * 10
     * 进行秒杀
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/do_seckill")
    public String seckill(Model model, User user,@RequestParam("goodsId")long goodsId) {
        model.addAttribute("user",user);

        if(user == null) {
            return "login";
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0) {
            model.addAttribute("ErrorMsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "seckill_fail";
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("ErrorMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "seckill_fail";
        }

        //减库存下订单，写入订单
        OrderInfo orderInfo = seckillService.seckill(user, goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);
        return "order_detail";
    }

    /**
     * 2.0版
     * QPS:1306
     * 线程：5000 * 10
     * 订单页面静态化
     * */
    @RequestMapping(value="/seckill", method= RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> seckillStatic(User user, @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);//10个商品，req1 req2
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);
    }

    /**
     * 3.0版
     * QPS:1658
     * 线程：5000 * 10
     * 加入消息队列
     * */
    @RequestMapping(value="/seckill_mq")
    @ResponseBody
    public Result<Integer> seckillMq(User user, @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);//10
        if (stock < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //压入消息队列
        //入队
        SeckillMessage sm = new SeckillMessage();
        sm.setUser(user);
        sm.setGoodsId(goodsId);
        sender.sendSeckillMessage(sm);
        return Result.success(0);//排队中
    }

    /**
     * 系统启动及初始化
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goods.getId(), goods.getStockCount(),1800);
            localOverMap.put(goods.getId(),false);
        }
    }

    /**
     * 客户端轮询查询是否下单成功
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(@RequestParam("goodsId") long goodsId,User user) {
        if (user == null) {
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
