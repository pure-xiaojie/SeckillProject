package com.xiaojie.controller;

import com.xiaojie.pojo.User;
import com.xiaojie.rabbitmq.MQSender;
import com.xiaojie.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: SeckillProject
 * @description: 测试
 * @author: Mr.Li
 * @create: 2020-05-29 14:29
 **/
@Controller
@RequestMapping("/test")
public class SampleController {
    @Autowired
    private MQSender sender;

    /**
     * 测试
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public Result<User> thymeleaf(Model model, User user) {
        return Result.success(user);
    }

    /**
     * 消息队列测试:发送字符串
     * @return
     */
    @RequestMapping("/mqStr")
    @ResponseBody
    public Result sendString() {
        sender.sendStr("string send test");
        return Result.success("success");
    }

    /**
     * 消息队列测试:Topic模式 交换机Exchange
     * @return
     */
    @RequestMapping("/mqTop")
    @ResponseBody
    public Result sendTop() {
        sender.sendTopic("top send test");
        return Result.success("success");
    }
}
