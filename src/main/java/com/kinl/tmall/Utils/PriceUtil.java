package com.kinl.tmall.Utils;

import com.kinl.tmall.pojo.Order;
import com.kinl.tmall.pojo.Orderitem;
import com.kinl.tmall.service.OrderItemService;
import com.kinl.tmall.service.ProductService;
import com.kinl.tmall.service.impl.OrderItemServiceImpl;
import com.kinl.tmall.service.impl.ProductServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

//    private static OrderItemService orderItemService = (OrderItemService)getBean("OrderItemService");
//
//    private static ProductService productService = (ProductService)getBean("ProductService");

    //private static OrderItemService orderItemService = (OrderItemService)getBean("OrderItemService");



    //查询order价格
    public static float orderFindPrice(Order order){
        OrderItemServiceImpl orderItemService = (OrderItemServiceImpl)applicationContext.getBean("orderItemServiceImpl");
        ProductServiceImpl productService = (ProductServiceImpl)applicationContext.getBean("productServiceImpl");
        List<Orderitem> orderitemList = orderItemService.findByOid(order.getId());
        float price = 0;
        for (Orderitem orderitem : orderitemList) {
            Float promoteprice = productService.findPromotepriceById(orderitem.getPid());
            float number = (float)orderitem.getNumber();
            price = (promoteprice*number)+price;
        }
        return price;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        PriceUtil.applicationContext = applicationContext;
    }

   /* private static Object getBean(String benaName){
        return applicationContext.getBean(benaName);
    }*/

}
