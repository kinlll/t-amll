package com.kinl.tmall.service.impl;

import com.kinl.tmall.dao.OrderitemMapper;
import com.kinl.tmall.pojo.Orderitem;
import com.kinl.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderitemMapper orderitemMapper;

    @Override
    public List<Orderitem> findByOid(Integer oid) {
        List<Orderitem> orderitemList = orderitemMapper.findByOid(oid);
        return orderitemList;
    }

    @Override
    public Integer countNumByOid(Integer oid) {
        List<Orderitem> orderitemList = orderitemMapper.findByOid(oid);
        Integer count=0;
        for (Orderitem orderitem : orderitemList) {
            Integer number = orderitem.getNumber();
            count+=number;
        }
        return count;
    }
}
