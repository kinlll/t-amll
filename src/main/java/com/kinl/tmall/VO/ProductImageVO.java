package com.kinl.tmall.VO;

import java.util.List;

public class ProductImageVO {
    private List<String> simpleImage;

    private List<String> detailsImage;

    public List<String> getSimpleImage() {
        return simpleImage;
    }

    public void setSimpleImage(List<String> simpleImage) {
        this.simpleImage = simpleImage;
    }

    public List<String> getDetailsImage() {
        return detailsImage;
    }

    public void setDetailsImage(List<String> detailsImage) {
        this.detailsImage = detailsImage;
    }

    public ProductImageVO(List<String> simpleImage, List<String> detailsImage) {
        this.simpleImage = simpleImage;
        this.detailsImage = detailsImage;
    }
}
