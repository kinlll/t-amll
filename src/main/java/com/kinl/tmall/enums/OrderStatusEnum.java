package com.kinl.tmall.enums;

public enum OrderStatusEnum {
    /*  订单状态  waitPay waitDelivery waitConfirm waitReview complete delete  */

    WAITPAY("waitPay"),
    WAITDELIVERY("waitDelivery"),
    WAITCONFIRM("waitConfirm"),
    WAITREVIEW("waitReview"),
    COMPLETE("complete"),
    DELETE("delete"),
    ;

    private String status;

    OrderStatusEnum(String status) {
        this.status = status;
    }
}
