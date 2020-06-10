package com.kinl.tmall.service;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.VO.PropertyVO;
import com.kinl.tmall.pojo.Property;

import java.util.List;
import java.util.Map;

public interface PropertyService {

    Page queryPage(Map<String,Object> map);

    Integer add(Property property);

    Property findById(Integer id);

    Integer update(Property property);

    Integer deleteById(Integer id);

    List<Property> findByCid(Integer cid);

    Integer addVO(PropertyVO property);
}
