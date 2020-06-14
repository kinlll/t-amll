package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.Property;

public class PropertyValueVO {

    private int id;
    //ptid
    private Property property;
    //pid
    private Product product;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
