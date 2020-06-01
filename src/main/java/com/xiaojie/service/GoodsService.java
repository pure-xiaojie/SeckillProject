package com.xiaojie.service;

import com.xiaojie.pojo.GoodsVo;

import java.util.List;

public interface GoodsService {

    /**
     * 查询秒杀商品列表
     * @return
     */
    public List<GoodsVo> listGoodVo();

    /**
     * 根据id获取秒杀商品信息
     * @param goodsId
     * @return
     */
    public GoodsVo getGoodsVoById(long goodsId);

    /**
     * 减少库存
     * @param goods
     */
    public void reduceStock(GoodsVo goods);
}
