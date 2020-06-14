package com.kinl.tmall.controller.admin;

import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.PropertyValueVO;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.pojo.Product;
import com.kinl.tmall.pojo.Propertyvalue;
import com.kinl.tmall.service.ProductService;
import com.kinl.tmall.service.PropertyValueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class PropertyValueController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyValueService propertyValueService;

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
}
