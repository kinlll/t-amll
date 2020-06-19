package com.kinl.tmall.comparator;

import com.kinl.tmall.pojo.Product;

import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getPromoteprice().compareTo(o2.getPromoteprice());
    }
}
