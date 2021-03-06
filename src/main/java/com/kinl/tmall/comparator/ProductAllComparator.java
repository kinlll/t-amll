package com.kinl.tmall.comparator;

import com.kinl.tmall.pojo.Product;

import java.util.Comparator;

public class ProductAllComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p1.getReviewCount() * p1.getSaleCount() - p2.getSaleCount() * p2.getReviewCount();
    }
}
