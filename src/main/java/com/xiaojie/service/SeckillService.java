package com.xiaojie.service;

import com.xiaojie.pojo.GoodsVo;
import com.xiaojie.pojo.OrderInfo;
import com.xiaojie.pojo.User;
import org.springframework.transaction.annotation.Transactional;

public interface SeckillService {
    /**
     * 秒杀业务
     * @param user
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo);

    /**
     * 轮询查询是否下单成功
     * @param id
     * @param goodsId
     * @return
     */
    public long getSeckillResult(int id, long goodsId);

    /**
     * 秒杀地址生成
     * @param user
     * @param goodsId
     * @return
     */
    public String createPath(User user, long goodsId);

    /**
     * 秒杀地址验证
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(User user, long goodsId,String path);
}
