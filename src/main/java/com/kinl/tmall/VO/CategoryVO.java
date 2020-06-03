package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.Product;

import java.io.Serializable;
import java.util.List;

public class CategoryVO implements Serializable {

    private Integer id;

    private String name;

    private List<Product> products;

    //redis里的首页的自定义标签
    private List<ProductRedisVO> data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ProductRedisVO> getData() {
        return data;
    }

    public void setData(List<ProductRedisVO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CategoryVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + products +
                ", data=" + data +
                '}';
    }
}