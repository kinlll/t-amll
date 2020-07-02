package com.kinl.tmall.controller.admin;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.enums.OrderStatusEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Order;
import com.kinl.tmall.service.OrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/pageOrder")
    public ResultVO pageOrder(@RequestParam(name = "pageIndex", defaultValue = "1", required = false) Integer pageIndex,
                              @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize){
        try {
            pageIndex = pageIndex < 0 ? 0 : pageIndex;
            Map<String, Object> map = new HashMap<>();
            map.put("pageIndex", pageIndex);
            map.put("pageSize", pageSize);

            Page page = orderService.queryPage(map);
            return ResultVOUtil.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"查询订单失败");
        }
    }

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/orders")
    public ResultVO orders(@RequestParam(name = "start", defaultValue = "1", required = false) Integer pageIndex,
                           @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize){
        try {
            pageIndex = pageIndex < 0 ? 0 : pageIndex;
            HashMap<String, Object> map = new HashMap<>();
            map.put("pageIndex", pageIndex);
            map.put("pageSize", pageSize);

            Page page = orderService.queryPageAdmin(map);
            return ResultVOUtil.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "查找订单失败");
        }
    }

    @RequiresPermissions(value = {"admin:select"})
    @PutMapping("/deliveryOrder/{id}")
    public ResultVO deliveryOrder(@PathVariable Integer id){
        try {
            Order order1 = orderService.findById(id);
            if (order1 == null) {
                return ResultVOUtil.error(1, "没有该订单");
            }
            if (!OrderStatusEnum.WAITDELIVERY.getStatus().equals(order1.getStatus())){
                return ResultVOUtil.error(1, "该订单已发货");
            }
            order1.setStatus(OrderStatusEnum.WAITCONFIRM.getStatus());
            orderService.updateStatus(order1);
            return ResultVOUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "发货失败");
        }
    }



    public ResultVO orderDelivery(@PathVariable Integer orderId){
        try {
            Order order = orderService.findById(orderId);
            if ("待发货".equals(order.getStatus())){
                order.setStatus("待收货");
                orderService.updateStatus(order);
                return ResultVOUtil.success();
            } else {
                return ResultVOUtil.error(1,"不是待发货状态");
            }
        } catch (AllException a) {
            a.printStackTrace();
            return ResultVOUtil.error(1,a.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1,"操作失败");
        }
    }

}
