package com.kinl.tmall.comparator;

import com.kinl.tmall.pojo.Product;

import java.util.Comparator;

public class ProductDataComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p1.getCreatedate().compareTo(p2.getCreatedate());
    }
}
