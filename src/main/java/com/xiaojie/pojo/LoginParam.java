package com.xiaojie.pojo;

import com.xiaojie.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @program: SeckillProject
 * @description: 登录实体参数封装
 * @author: Mr.Li
 * @create: 2020-05-30 15:59
 **/
@Getter
@Setter
@ToString
public class LoginParam {
    @NotNull(message = "手机号不能为空")
    @IsMobile()
    private String mobile;
    @NotNull(message="密码不能为空")
    @Length(min = 23, message = "密码长度需要在7个字以内")
    private String password;
}
