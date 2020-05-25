package com.kinl.tmall.service.impl;

import com.kinl.tmall.dao.ProductMapper;
import com.kinl.tmall.dao.PropertyMapper;
import com.kinl.tmall.dao.PropertyvalueMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.Property;
import com.kinl.tmall.pojo.Propertyvalue;
import com.kinl.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    private PropertyvalueMapper propertyvalueMapper;

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Propertyvalue findByPtid(Integer ptid) {
        Propertyvalue propertyvalue = propertyvalueMapper.findByPtid(ptid);
        return propertyvalue;
    }

    @Transactional
    @Override
    public void updateValue(Integer pid, Map<Integer, String> map) {
        int result;
        Product product = productMapper.selectByPrimaryKey(pid);
        Set<Integer> propertyIds = map.keySet();
        for (Integer id : propertyIds) {
            Property property = propertyMapper.selectByPrimaryKey(id);
            if (property == null)
                throw new AllException(ResultEnum.NOEXIST_PROPERTY);
            if (property.getCid().equals(product.getCid())){
                Propertyvalue propertyvalue1 = propertyvalueMapper.findByPtid(property.getId());
                if (propertyvalue1 == null) {
                    Propertyvalue propertyvalue = new Propertyvalue();
                    propertyvalue.setPid(pid);
                    propertyvalue.setPtid(property.getId());
                    propertyvalue.setValue(map.get("id"));
                    result = propertyvalueMapper.insert(propertyvalue);
                    if (result == 0)
                        throw new AllException(ResultEnum.PROPERTYVALUE_UPFATE_ERROR);
                } else {
                    Propertyvalue propertyvalue = new Propertyvalue();
                    propertyvalue.setValue(map.get(id));
                    propertyvalue.setPtid(property.getId());
                    propertyvalueMapper.updateByPtid(propertyvalue);
                }
            } else {
                throw new AllException(ResultEnum.PRODUCT_NOEXIST_PROPERTY);
            }
        }
    }

    @Override
    public List<Propertyvalue> findByPid(Integer pid) {
        List<Propertyvalue> propertyvalues = propertyvalueMapper.findbyPid(pid);
        return propertyvalues;
    }
}
