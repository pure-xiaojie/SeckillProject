package com.xiaojie.pojo;

import lombok.*;

/**
 * @program: SeckillProject
 * @description: 秒杀订单实体类
 * @author: Mr.Li
 * @create: 2020-05-31 9:43
 **/

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SeckillOrder {
    private Long id;

    private Long userId;

    private Long orderId;

    private Long goodsId;

}