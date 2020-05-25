package com.kinl.tmall.VO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderVO {

    private Integer id;

    //订单状态
    private String status;

    //订单价格
    private float price;

    //商品数量
    private Integer number;

    //买家名称
    private String name;

    //创建订单时间
    private String createdate;

    //支付时间
    private String paydate;

    //发货时间
    private String deliverydate;

    //确认收货时间
    private String confirmdate;

    //订单详情
    private List<OrderItemVO> orderItemVOList;
}
