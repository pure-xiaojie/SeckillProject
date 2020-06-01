package com.xiaojie.exception;

import com.xiaojie.result.CodeMsg;

/**
 * @program: SeckillProject
 * @description: 异常封装
 * @author: Mr.Li
 * @create: 2020-05-30 16:54
 **/
public class GlobalException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }
    public CodeMsg getCm() {
        return cm;
    }
}
