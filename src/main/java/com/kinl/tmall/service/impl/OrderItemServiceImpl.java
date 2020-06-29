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



    //List<Orderitem> 转 List<OrderItemForeVO>
    private List<OrderItemForeVO> orderitem2OrderItemForeVO(List<Orderitem> orderitems){
        List<OrderItemForeVO> orderItemForeVOS = new ArrayList<>();
        for (Orderitem orderitem : orderitems) {
            OrderItemForeVO orderItemForeVO = new OrderItemForeVO();
            User user = userService.findById(orderitem.getUid());
            Product product = productService.findById(orderitem.getPid());

            orderItemForeVO.setId(orderitem.getId());
            orderItemForeVO.setNumber(orderitem.getNumber());
            orderItemForeVO.setUser(user);
            orderItemForeVO.setProduct(product);
            if (orderitem.getOid() == null) {
                orderItemForeVOS.add(orderItemForeVO);
            }
        }
        return orderItemForeVOS;
    }

    @Override
    public List<OrderItemForeVO> findByUidAndNoOid(Integer uid) {
        OrderitemExample orderitemExample = new OrderitemExample();
        OrderitemExample.Criteria criteria = orderitemExample.createCriteria();
        criteria.andUidEqualTo(uid);
        List<Orderitem> orderitems = orderitemMapper.selectByExample(orderitemExample);
        List<OrderItemForeVO> orderItemForeVOS = orderitem2OrderItemForeVO(orderitems);
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

    @Override
    public Orderitem findByUidAndPid(Integer uid, Integer pid) {
        Orderitem orderitem = orderitemMapper.findByUidAndPid(uid, pid);
        return orderitem;
    }

    @Override
    public Integer updateNumByUidAndPid(Integer uid, Integer pid, Integer num) {
        Integer integer = orderitemMapper.updateNumByUidAndPid(uid, pid, num);
        if (integer == 0) {
            throw new AllException(ResultEnum.UPDATE_ORDERITEM_ERROR);
        }
        return integer;
    }

    @Override
    public Integer updateNumByUidAndPidInsert(Integer uid, Integer pid, Integer num) {
        Integer integer = orderitemMapper.updateNumByUidAndPidInsert(uid, pid, num);
        if (integer == 0) {
            throw new AllException(ResultEnum.UPDATE_ORDERITEM_ERROR);
        }
        return integer;
    }

    @Override
    public Orderitem findById(Integer oiid) {
        Orderitem orderitem = orderitemMapper.selectByPrimaryKey(oiid);
        return orderitem;
    }

    @Override
    public Integer deleteById(Integer id) {
        int i = orderitemMapper.deleteByPrimaryKey(id);
        if (i == 0) {
            throw new AllException(ResultEnum.DELETE_ORDERITEM_ERROR);
        }
        return i;
    }

    @Override
    public List<OrderItemForeVO> findByIds(int[] oiid) {
        List<Orderitem> orderitems = new ArrayList<>();
        for (int i = 0; i < oiid.length; i++){
            Orderitem orderitem = orderitemMapper.selectByPrimaryKey(oiid[i]);
            if (orderitem == null) {
                throw new AllException(ResultEnum.ORDERITEM_NOEXIT);
            }
            orderitems.add(orderitem);
        }
        List<OrderItemForeVO> orderItemForeVOS = orderitem2OrderItemForeVO(orderitems);
        return orderItemForeVOS;
    }

    @Override
    public float getTotal(List<OrderItemForeVO> orderItemForeVOS) {
        float total = 0;
        for (OrderItemForeVO orderItemForeVO : orderItemForeVOS) {
            Float promoteprice = orderItemForeVO.getProduct().getPromoteprice();
            Integer number = orderItemForeVO.getNumber();
            float v = promoteprice * number;
            total += v;
        }
        return total;
    }

    @Override
    public Integer updateById(OrderItemForeVO orderItemForeVO) {
        Integer integer = orderitemMapper.updateById(orderItemForeVO);
        if (integer == 0){
            throw new AllException(ResultEnum.UPDATE_ORDERITEM_ERROR);
        }
        return integer;
    }
}
