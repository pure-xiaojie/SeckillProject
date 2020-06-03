package com.xiaojie.rabbitmq;

import com.xiaojie.pojo.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: SeckillProject
 * @description: 秒杀消息封装
 * @author: Mr.Li
 * @create: 2020-06-03 09:52
 **/
@Getter
@Setter
public class SeckillMessage {
    private User user;
    private long goodsId;
}
