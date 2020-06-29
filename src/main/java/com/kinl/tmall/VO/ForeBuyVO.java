package com.kinl.tmall.VO;

import java.util.List;

public class ForeBuyVO<T> {

    private List<T> orderItems;

    private float total;

    private Integer oid;

    public List<T> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<T> orderItems) {
        this.orderItems = orderItems;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }
}
