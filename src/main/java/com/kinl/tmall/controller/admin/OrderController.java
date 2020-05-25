package com.kinl.tmall.controller.admin;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.Order;
import com.kinl.tmall.service.OrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/pageOrder")
    public ResultVO pageOrder(@RequestParam(name = "pageIndex", defaultValue = "1", required = false) Integer pageIndex,
                              @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize){
        try {
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

    @RequiresPermissions(value = {"admin:update"})
    @PostMapping("/orderDelivery/{orderId}")
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
