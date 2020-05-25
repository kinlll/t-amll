package com.kinl.tmall.VO;

import lombok.Data;

@Data
public class OrderItemVO {

    private Integer pid;

    private String name;

    private Float simplePrice;

    private Integer number;

    //单个图片图片地址
    private String simpleImage;

}
