package com.xiaojie.access;

import com.xiaojie.pojo.User;

/**
 * @program: SeckillProject
 * @description: 保存用户对象，与当前线程绑定
 * @author: Mr.Li
 * @create: 2020-06-03 20:19
 **/
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}
