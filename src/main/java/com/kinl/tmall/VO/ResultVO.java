package com.kinl.tmall.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -5644975618653920523L;

    private Integer code;

    private String message;

    private T data;

    public void resultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultVO() {
    }
}
