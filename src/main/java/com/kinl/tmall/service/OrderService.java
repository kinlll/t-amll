package com.kinl.tmall.service;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.VO.ForeOrderVO;
import com.kinl.tmall.VO.OrderItemForeVO;
import com.kinl.tmall.pojo.Order;
import com.kinl.tmall.pojo.User;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Page queryPage(Map<String, Object> map) throws ParseException;

    Order findById(Integer id);

    Integer updateStatus(Order order);

    List<OrderItemForeVO> createOrder(ForeOrderVO orderVO, User user, HttpSession session);

    Integer waitDelivery(Integer oid);

    List<ForeOrderVO> findByUid(Integer uid);

    Page queryPageAdmin(HashMap<String, Object> map);

    Integer update(Order order);

    ForeOrderVO findVOById(Integer oid);
}
