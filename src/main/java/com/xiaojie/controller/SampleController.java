package com.xiaojie.controller;

import com.xiaojie.pojo.User;
import com.xiaojie.redis.RedisService;
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

    @RequestMapping("/userInfo")
    @ResponseBody
    public Result<User> thymeleaf(Model model, User user) {
        return Result.success(user);
    }
}
