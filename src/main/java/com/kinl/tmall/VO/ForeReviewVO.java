package com.kinl.tmall.VO;

import com.kinl.tmall.pojo.Order;
import com.kinl.tmall.pojo.Product;
import lombok.Data;

import java.util.List;

@Data
public class ForeReviewVO {

    private Product product;

    private Order order;

    private List<ReviewVO> reviews;
}
