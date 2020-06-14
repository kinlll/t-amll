package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.Category;
import com.kinl.tmall.pojo.Productimage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductVO implements Serializable {
    private Integer id;
    private String name;
    private String subTitle;
    private Float originalPrice;
    private Float promotePrice;
    private Integer stock;
    private Integer cid;
    private Date createdate;
    private Productimage firstProductImage;

    private List<Productimage> productSingleImages;

    private List<Productimage> productDetailImages;

    private Category category;

    private static final long serialVersionUID = 1L;

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
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Float getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(Float promotePrice) {
        this.promotePrice = promotePrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Productimage getFirstProductImage() {
        return firstProductImage;
    }

    public void setFirstProductImage(Productimage firstProductImage) {
        this.firstProductImage = firstProductImage;
    }

    public List<Productimage> getProductSingleImages() {
        return productSingleImages;
    }

    public void setProductSingleImages(List<Productimage> productSingleImages) {
        this.productSingleImages = productSingleImages;
    }

    public List<Productimage> getProductDetailImages() {
        return productDetailImages;
    }

    public void setProductDetailImages(List<Productimage> productDetailImages) {
        this.productDetailImages = productDetailImages;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
