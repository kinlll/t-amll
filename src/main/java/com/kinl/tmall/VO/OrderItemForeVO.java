package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.Order;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.User;

public class OrderItemForeVO {

    private Integer id;

    private Product product;

    private Order order;

    private User user;

    private Integer number;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
