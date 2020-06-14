package com.kinl.tmall.service;

import com.kinl.tmall.pojo.Productimage;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public interface ProductimageService {

    Integer add(Productimage productimage);

    ArrayList<Integer> findByIdAndSimple(Integer id);

    ArrayList<Integer> findByIdAndDetails(Integer id);

    String findFirstImage(Integer pid);

    Productimage findFirstImageByPid(Integer pid);

    List<Productimage> findAllSimpleByPid(Integer pid);

    List<Productimage> findAllDetailsByPid(Integer pid);

    Productimage findById(Integer id);

    void deleteById(Integer id, HttpServletRequest request);
}
