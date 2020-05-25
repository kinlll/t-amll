package com.kinl.tmall.service;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.pojo.Order;

import java.text.ParseException;
import java.util.Map;

public interface OrderService {

    Page queryPage(Map<String, Object> map) throws ParseException;

    Order findById(Integer id);

    Integer updateStatus(Order order);
}
