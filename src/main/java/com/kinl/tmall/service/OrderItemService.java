package com.kinl.tmall.service;

import com.kinl.tmall.VO.OrderItemForeVO;
import com.kinl.tmall.pojo.Orderitem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemService {

    List<Orderitem> findByOid(Integer oid);

    Integer countNumByOid(Integer oid);

    Integer countByPid(Integer pid);

    List<OrderItemForeVO> findByUidAndNoOid(Integer uid);

    Integer insertNoOid(Orderitem orderitem);

    Orderitem findByUidAndPid(Integer uid, Integer pid);

    Integer updateNumByUidAndPid(Integer uid, Integer pid, Integer num);

    Integer updateNumByUidAndPidInsert(Integer uid, Integer pid, Integer num);

    Orderitem findById(Integer oiid);

    Integer deleteById(Integer id);

    List<OrderItemForeVO> findByIds(int[] oiid);

    float getTotal(List<OrderItemForeVO> orderItemForeVOS);

    Integer updateById(OrderItemForeVO orderItemForeVO);

    List<OrderItemForeVO> findVOByOid(Integer oid);

    Integer getTotalNumber(List<OrderItemForeVO> orderItemForeVOS);
}
