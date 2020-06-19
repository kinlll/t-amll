package com.kinl.tmall.comparator;

import com.kinl.tmall.pojo.Product;

import java.util.Comparator;

public class ProductSaleCountComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getSaleCount() - o2.getSaleCount();
    }
}
