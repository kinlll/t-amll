package com.kinl.tmall.service.impl;

import com.kinl.tmall.Utils.DateFormatUtil;
import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.PriceUtil;
import com.kinl.tmall.VO.*;
import com.kinl.tmall.dao.OrderMapper;
import com.kinl.tmall.enums.OrderStatusEnum;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Order;
import com.kinl.tmall.pojo.Orderitem;
import com.kinl.tmall.pojo.User;
import com.kinl.tmall.service.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductimageService productimageService;

    @Override
    public Page queryPage(Map<String, Object> map) throws ParseException {
        Page page = new Page((Integer) map.get("pageSize"), (Integer) map.get("pageIndex"));
        Integer start = page.getStart();
        map.put("start", start);
        List<Order> orderList = orderMapper.pageQuery(map);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);

            orderVO.setConfirmdate(DateFormatUtil.changeDateFormat(order.getConfirmdate()));
            orderVO.setCreatedate(DateFormatUtil.changeDateFormat(order.getCreatedate()));
            orderVO.setDeliverydate(DateFormatUtil.changeDateFormat(order.getDeliverydate()));
            orderVO.setPaydate(DateFormatUtil.changeDateFormat(order.getPaydate()));

            orderVO.setPrice(PriceUtil.orderFindPrice(order));
            String userName = userService.findNameById(order.getUid());
            orderVO.setName(userName);
            Integer number = orderItemService.countNumByOid(order.getId());
            orderVO.setNumber(number);

            List<Orderitem> orderitemList = orderItemService.findByOid(order.getId());
            List<OrderItemVO> orderItemVOS = new ArrayList<>();
            for (Orderitem orderitem : orderitemList) {
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setName(productService.findNameById(orderitem.getPid()));
                orderItemVO.setNumber(orderitem.getNumber());
                orderItemVO.setPid(orderitem.getPid());
                orderItemVO.setSimplePrice(productService.findPromotepriceById(orderitem.getPid()));
                orderItemVO.setSimpleImage(productimageService.findFirstImage(orderitem.getPid()));
                orderItemVOS.add(orderItemVO);
            }
            orderVO.setOrderItemVOList(orderItemVOS);
            orderVOList.add(orderVO);
        }
        page.setDatas(orderVOList);
        Integer count = orderMapper.count();
        page.setRecord(count);
        page.setTotalPageCount(count);
        return page;
    }

    @Override
    public Order findById(Integer id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        return order;
    }

    @Override
    public Integer updateStatus(Order order) {
        order.setDeliverydate(new Date());
        Integer integer = orderMapper.updateStatus(order);
        if (integer == 0){
            throw new AllException(ResultEnum.ADD_ORDER_STATUS_ERROR);
        }
        return integer;
    }

    private String getOrderCode(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyHHddHHmmss", Locale.CHINA);
        String format = simpleDateFormat.format(new Date());
        for (int i = 0; i < 4; i++){
            int random = RandomUtils.nextInt(10);
            format+=random;
        }
        return format;
    }

    @Override
    public List<OrderItemForeVO> createOrder(ForeOrderVO orderVO, User user, HttpSession session) {
        orderVO.setCreateDate(new Date());
        orderVO.setStatus(OrderStatusEnum.WAITPAY.getStatus());
        orderVO.setUid(user.getId());
        orderVO.setOrderCode(getOrderCode());
        Integer integer = orderMapper.insertForeVO(orderVO);
        if (integer == 0){
            throw new AllException(ResultEnum.ADD_ORDER_ERROR);
        }
        Order order = new Order();
        BeanUtils.copyProperties(orderVO, order);
        order.setConfirmdate(orderVO.getConfirmDate());
        order.setCreatedate(orderVO.getCreateDate());
        order.setDeliverydate(orderVO.getDeliveryDate());
        order.setOrdercode(orderVO.getOrderCode());
        order.setPaydate(orderVO.getPayDate());
        order.setUsermessage(orderVO.getUserMessage());
        List<OrderItemForeVO> orderItemForeVOS = (List<OrderItemForeVO>) session.getAttribute("ois");
        for (OrderItemForeVO orderItemForeVO : orderItemForeVOS) {
            orderItemForeVO.setOrder(order);
            orderItemService.updateById(orderItemForeVO);
        }
        return orderItemForeVOS;
    }

    @Override
    public Integer waitDelivery(Integer oid) {

        Integer integer = orderMapper.updateStatusAndPayDate(oid, new Date(), OrderStatusEnum.WAITDELIVERY.getStatus());
        if (integer == 0) {
            throw new AllException(ResultEnum.UPDATE_ORDER_ERROR);
        }
        return integer;
    }

    @Override
    public List<ForeOrderVO> findByUid(Integer uid) {
        List<ForeOrderVO> foreOrderVOS = orderMapper.findByUid(uid);
        for (ForeOrderVO foreOrderVO : foreOrderVOS) {
            List<OrderItemForeVO> orderItems = orderItemService.findVOByOid(foreOrderVO.getId());
            float total = orderItemService.getTotal(orderItems);
            Integer totalNumber = orderItemService.getTotalNumber(orderItems);
            foreOrderVO.setTotalNumber(totalNumber);
            foreOrderVO.setTotal(total);
            foreOrderVO.setOrderItems(orderItems);
        }
        return foreOrderVOS;
    }

    private String changeStatus(String status){
        String statusDesc = "";
        if (OrderStatusEnum.WAITPAY.getStatus().equals(status)){
                statusDesc = "待付款";
        }else if (OrderStatusEnum.WAITDELIVERY.getStatus().equals(status)){
            statusDesc = "待发货";
        }else if (OrderStatusEnum.WAITCONFIRM.getStatus().equals(status)){
            statusDesc = "待收货";
        }else if (OrderStatusEnum.WAITREVIEW.getStatus().equals(status)){
            statusDesc = "待评价";
        }else if (OrderStatusEnum.COMPLETE.getStatus().equals(status)){
            statusDesc = "完成";
        }else if (OrderStatusEnum.DELETE.getStatus().equals(status)){
            statusDesc = "删除";
        }
        return statusDesc;
    }

    @Override
    public Page queryPageAdmin(HashMap<String, Object> map) {
        Page page = new Page((Integer) map.get("pageSize"), (Integer)map.get("pageIndex"));
        Integer start = page.getStart();
        map.put("start", start);
        List<AdminOrderVO> adminOrderVOS = orderMapper.queryPageAdmin(map);
        for (AdminOrderVO adminOrderVO : adminOrderVOS) {
            User user = userService.findById(adminOrderVO.getUid());
            adminOrderVO.setUser(user);
            List<OrderItemForeVO> orderItemForeVOS = orderItemService.findVOByOid(adminOrderVO.getId());
            adminOrderVO.setOrderItems(orderItemForeVOS);
            adminOrderVO.setTotal(orderItemService.getTotal(orderItemForeVOS));
            adminOrderVO.setTotalNumber(orderItemService.getTotalNumber(orderItemForeVOS));
            adminOrderVO.setStatusDesc(changeStatus(adminOrderVO.getStatus()));
        }
        page.setDatas(adminOrderVOS);
        Integer count = orderMapper.count();
        page.setRecord(count);
        page.setTotalPageCount(count);
        return page;
    }

    @Override
    public Integer update(Order order) {
        int i = orderMapper.updateByPrimaryKey(order);
        if (i == 0) {
            throw new AllException(ResultEnum.UPDATE_ORDER_ERROR);
        }
        return i;
    }

    @Override
    public ForeOrderVO findVOById(Integer oid) {
        ForeOrderVO foreOrderVO = orderMapper.findVOById(oid);
        if (foreOrderVO == null) {
            throw new AllException(ResultEnum.FIND_OREDER_ERROR);
        }
        List<OrderItemForeVO> orderItemForeVOS = orderItemService.findVOByOid(oid);
        foreOrderVO.setOrderItems(orderItemForeVOS);
        foreOrderVO.setTotal(orderItemService.getTotal(orderItemForeVOS));
        foreOrderVO.setTotalNumber(orderItemService.getTotalNumber(orderItemForeVOS));
        return foreOrderVO;
    }
}
