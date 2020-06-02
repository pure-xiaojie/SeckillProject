package com.xiaojie.service.impl;

import com.xiaojie.dao.GoodsMapper;
import com.xiaojie.pojo.GoodsVo;
import com.xiaojie.pojo.SeckillGoods;
import com.xiaojie.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: SeckillProject
 * @description: 秒杀商品服务
 * @author: Mr.Li
 * @create: 2020-05-31 09:55
 **/
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 秒杀商品查询
     * @return
     */
    public List<GoodsVo> listGoodVo() {
        return goodsMapper.listGoodsVo();
    }

    /**
     * 根据商品id获取秒杀商品信息
     * @param goodsId
     * @return
     */
    public GoodsVo getGoodsVoById(long goodsId) {
        return goodsMapper.getGoodsVoById(goodsId);
    }

    /**
     * 减少库存
     * @param goods
     */
    public int reduceStock(GoodsVo goods) {
        long goodsId = goods.getId();
        return goodsMapper.reduceStock(goodsId);
    }
}
