package com.kinl.tmall.service.impl;

import com.kinl.tmall.VO.OrderItemForeVO;
import com.kinl.tmall.dao.OrderitemMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.*;
import com.kinl.tmall.service.OrderItemService;
import com.kinl.tmall.service.OrderService;
import com.kinl.tmall.service.ProductService;
import com.kinl.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderitemMapper orderitemMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

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

    @Override
    public Integer countByPid(Integer pid) {
        int count = 0;
        OrderitemExample orderitemExample = new OrderitemExample();
        OrderitemExample.Criteria criteria = orderitemExample.createCriteria();
        criteria.andPidEqualTo(pid);
        List<Orderitem> orderitems = orderitemMapper.selectByExample(orderitemExample);
        for (Orderitem orderitem : orderitems) {
            Integer number = orderitem.getNumber();
            count+=number;
        }
        return count;
    }

    @Override
    public List<OrderItemForeVO> findByUid(Integer uid) {
        OrderitemExample orderitemExample = new OrderitemExample();
        OrderitemExample.Criteria criteria = orderitemExample.createCriteria();
        criteria.andUidEqualTo(uid);
        List<Orderitem> orderitems = orderitemMapper.selectByExample(orderitemExample);
        List<OrderItemForeVO> orderItemForeVOS = new ArrayList<>();
        for (Orderitem orderitem : orderitems) {
            OrderItemForeVO orderItemForeVO = new OrderItemForeVO();
            User user = userService.findById(orderitem.getUid());
            Product product = productService.findById(orderitem.getPid());
            Order order = orderService.findById(orderitem.getOid());

            orderItemForeVO.setId(orderitem.getId());
            orderItemForeVO.setNumber(orderitem.getNumber());
            orderItemForeVO.setUser(user);
            orderItemForeVO.setOrder(order);
            orderItemForeVO.setProduct(product);
        }

        return orderItemForeVOS;
    }

    @Override
    public Integer insertNoOid(Orderitem orderitem) {
        Integer integer = orderitemMapper.insertNoOid(orderitem);
        if (integer == 0) {
            throw new AllException(ResultEnum.INSERT_ORDERITEM_ERROR);
        }
        return integer;
    }
}
