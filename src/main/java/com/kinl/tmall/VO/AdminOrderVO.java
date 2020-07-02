package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdminOrderVO {

    private Integer id;

    //创建订单时间
    private Date createDate;

    //支付时间
    private Date payDate;

    //发货时间
    private Date deliveryDate;

    //确认收货时间
    private Date confirmDate;

    //订单金额
    private float total;

    private String status;

    //订单商品数量
    private Integer totalNumber;

    private Integer uid;

    private User user;

    private String statusDesc;

    private List<OrderItemForeVO> orderItems;
}
