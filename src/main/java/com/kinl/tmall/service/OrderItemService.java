package com.kinl.tmall.service;

import com.kinl.tmall.pojo.Orderitem;

import java.util.List;

public interface OrderItemService {

    List<Orderitem> findByOid(Integer oid);

    Integer countNumByOid(Integer oid);

    Integer countByPid(Integer pid);
}
