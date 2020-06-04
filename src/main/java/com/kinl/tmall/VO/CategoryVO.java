package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.Product;

import java.io.Serializable;
import java.util.List;

public class CategoryVO implements Serializable {

    private Integer id;

    private String name;

    private List<Product> products;

    //一个分类有多行
    private List<List<Product>> data;

    //一行里有多个产品
    private List<Product> productsByRow;

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

    public List<List<Product>> getData() {
        return data;
    }

    public void setData(List<List<Product>> data) {
        this.data = data;
    }

    public List<Product> getProductsByRow() {
        return productsByRow;
    }

    public void setProductsByRow(List<Product> productsByRow) {
        this.productsByRow = productsByRow;
    }

    @Override
    public String toString() {
        return "CategoryVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + products +
                ", data=" + data +
                ", productsByRow=" + productsByRow +
                '}';
    }
}