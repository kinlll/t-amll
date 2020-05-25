package com.kinl.tmall.service.impl;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.dao.PropertyMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Property;
import com.kinl.tmall.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class
PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyMapper propertyMapper;

    @Override
    public Page queryPage(Map<String, Object> map) {
        Page page = new Page((Integer) map.get("pageSize"),(Integer)map.get("pageIndex"));
        Integer startIndex = page.getStartIndex();
        map.put("startIndex", startIndex);
        List<Property> properties = propertyMapper.queryPage(map);
        Integer count = propertyMapper.queryCount(map);
        page.setDatas(properties);
        page.setRecord(count);
        page.setTotalPageCount(count);
        return page;
    }

    @Override
    public Integer add(Property property) {
        Property p1 = propertyMapper.findByName(property.getName());
        if (p1 == null){
            int insert = propertyMapper.insert(property);
            if (insert == 0){
                throw new AllException(ResultEnum.ADD_PROPERTY_ERROR);
            }
            return insert;
        }else {
            throw new AllException(ResultEnum.PROPERTYEXIST);
        }
    }

    @Override
    public Property findById(Integer id) {
        return propertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer update(Property property) {
        Property property1 = propertyMapper.selectByPrimaryKey(property.getId());
        if (property1 == null) {
            throw new AllException(ResultEnum.PROPERTY_NOTEXIST);
        }
        Integer integer = propertyMapper.updateName(property);
        if (integer == 0){
            throw new AllException(ResultEnum.UPDATE_PROPERTY_ERROR);
        }
        return integer;
    }

    @Override
    public Integer deleteById(Integer id) {
        int i = propertyMapper.deleteByPrimaryKey(id);
        if (i == 0) {
            throw new AllException(ResultEnum.DELETE_PROPERTY_ERROR);
        }
        return i;
    }

    @Override
    public List<Property> findByCid(Integer cid) {
        List<Property> properties = propertyMapper.findByCid(cid);
        return properties;
    }
}
