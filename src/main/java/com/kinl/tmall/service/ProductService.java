package com.kinl.tmall.service;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.VO.ProductVO;
import com.kinl.tmall.pojo.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> findAll();

    List<Product> findByCidFive(Integer cid);

    Page pageQuery(Map<String, Object> map);

    Integer add(Product product);

    Product findById(Integer id);

    void update(Integer productId, Product product);

    Integer deleteById(Integer id);

    Float findPromotepriceById(Integer pid);

    String findNameById(Integer id);

    List<Product> findbyCid(Integer cid);

    Integer addVO(ProductVO product);
}
