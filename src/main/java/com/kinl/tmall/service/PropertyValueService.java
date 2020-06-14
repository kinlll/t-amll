package com.kinl.tmall.service;

import com.kinl.tmall.VO.PropertyValueVO;
import com.kinl.tmall.pojo.Propertyvalue;

import java.util.List;
import java.util.Map;

public interface PropertyValueService {

    Propertyvalue findByPtid(Integer ptid);

    void updateValue(Integer pid, Map<Integer, String> map);

    List<Propertyvalue> findByPid(Integer pid);

    List<PropertyValueVO> findVOByPid(Integer pid);
}
