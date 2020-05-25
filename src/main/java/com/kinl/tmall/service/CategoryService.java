package com.kinl.tmall.service;


import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.VO.CategoryVO;
import com.kinl.tmall.pojo.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Category> findAll();

    List<CategoryVO> finAllVO();

    Page queryPage(Map<String,Object> map);

    Integer addCategory(String categoryName);

    Integer deleteById(Integer id);

    Category findById(Integer id);

    Integer update(Category category);
}
