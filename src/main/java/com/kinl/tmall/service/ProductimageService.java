package com.kinl.tmall.service;

import com.kinl.tmall.pojo.Productimage;

import java.util.ArrayList;

public interface ProductimageService {

    Integer add(Productimage productimage);

    ArrayList<String> findByIdAndSimple(Integer id);

    ArrayList<String> findByIdAndDetails(Integer id);

    String findFirstImage(Integer pid);
}
