package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.Propertyvalue;

import java.util.List;

public class ForeProductVO {

    private Product product;

    private List<PropertyValueVO> pvs;

    private List<ReviewVO> reviews;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<PropertyValueVO> getPvs() {
        return pvs;
    }

    public void setPvs(List<PropertyValueVO> pvs) {
        this.pvs = pvs;
    }

    public List<ReviewVO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewVO> reviews) {
        this.reviews = reviews;
    }
}
