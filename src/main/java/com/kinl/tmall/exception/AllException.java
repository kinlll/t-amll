package com.kinl.tmall.exception;

import com.kinl.tmall.enums.ResultEnum;

public class AllException extends RuntimeException {

    private Integer code;

    public AllException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public AllException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
