package com.kinl.tmall.controller.admin;

import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.PropertyValueVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.Property;
import com.kinl.tmall.pojo.Propertyvalue;
import com.kinl.tmall.service.ProductService;
import com.kinl.tmall.service.PropertyService;
import com.kinl.tmall.service.PropertyValueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class PropertyValueController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private PropertyService propertyService;

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/products/{pid}/propertyValues")
    public ResultVO list(@PathVariable("pid") Integer pid){
        Product product = productService.findById(pid);
        if (product == null) {
            return ResultVOUtil.error(1,"找不到产品");
        }
        List<PropertyValueVO> propertyValueVOS = propertyValueService.findVOByPid(pid);
        return ResultVOUtil.success(propertyValueVOS);
    }

    @RequiresPermissions(value = {"admin:select"})
    @PutMapping("/propertyValues")
    public ResultVO put(@RequestBody PropertyValueVO propertyValueVO){
        try {
            Product product = productService.findById(propertyValueVO.getProduct().getId());
            if (product == null) {
                return ResultVOUtil.error(1,"没有该产品");
            }
            Property property = propertyService.findById(propertyValueVO.getProperty().getId());
            if (property == null) {
                return ResultVOUtil.error(1,"没有该属性");
            }
            Propertyvalue propertyvalue = propertyValueService.findByPidAndPtid(product.getId(), property.getId());
            if (propertyvalue == null) {
                propertyValueService.insert(propertyValueVO);
            }else {
                propertyValueService.update(propertyValueVO);
            }
            //Propertyvalue resultProperty = propertyValueService.findById(propertyValueVO.getId());
            Propertyvalue resultProperty = propertyValueService.findByPidAndPtid(product.getId(), property.getId());
            return ResultVOUtil.success(resultProperty);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, e.getMessage());
        }
    }
}
