package com.kinl.tmall.VO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ForeOrderVO {

    private Integer id;

    private Integer uid;

    //订单状态
    private String status;

    private String address;

    private String userMessage;

    private String orderCode;

    private String post;

    //收货人信息
    private String receiver;

    private String mobile;

    //创建订单时间
    private Date createDate;

    //支付时间
    private Date payDate;

    //发货时间
    private Date deliveryDate;

    //确认收货时间
    private Date confirmDate;

    private List<OrderItemForeVO> orderItems;

    //订单金额
    private float total;

    //订单商品数量
    private Integer totalNumber;
}
