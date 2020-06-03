package com.xiaojie.service;

import com.xiaojie.pojo.LoginParam;
import com.xiaojie.pojo.User;

import javax.servlet.http.HttpServletResponse;

public interface UserService {
    //token值
    public static final String COOKI_NAME_TOKEN = "token";

    /**
     * 登录
     * @param loginParam
     * @return
     */
    String login(HttpServletResponse response,LoginParam loginParam);

    /**
     * 获取token
     * @param response
     * @param token
     * @return
     */
    public User getByToken(HttpServletResponse response, String token);
}
