package com.kinl.tmall.VO;

import java.util.List;

public class ProductImageVO {
    private List simpleImage;

    private List detailsImage;

    public List getSimpleImage() {
        return simpleImage;
    }

    public void setSimpleImage(List simpleImage) {
        this.simpleImage = simpleImage;
    }

    public List getDetailsImage() {
        return detailsImage;
    }

    public void setDetailsImage(List detailsImage) {
        this.detailsImage = detailsImage;
    }

    public ProductImageVO(List<String> simpleImage, List<String> detailsImage) {
        this.simpleImage = simpleImage;
        this.detailsImage = detailsImage;
    }

    public ProductImageVO() {
    }
}
