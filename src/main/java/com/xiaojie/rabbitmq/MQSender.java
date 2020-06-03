package com.xiaojie.rabbitmq;

import com.xiaojie.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: SeckillProject
 * @description: 消息队列：发送
 * @author: Mr.Li
 * @create: 2020-06-03 09:51
 **/
@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate ;

    /**
     * 秒杀业务消息发送
     */
    public void sendSeckillMessage(SeckillMessage sm) {
        String msg = RedisService.beanToString(sm);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
    }

    /**
     * 字符串发送
     * @param message
     */
    public void sendStr(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}

    /**
     * Topic模式 交换机Exchange
     */
	public void sendTopic(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}
}

