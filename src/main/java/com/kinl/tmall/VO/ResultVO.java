package com.kinl.tmall.VO;

import lombok.Data;

@Data
public class ResultVO<T> {

    private Integer code;

    private String message;

    private T data;

    public void resultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
