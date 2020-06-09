package com.kinl.tmall.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * 所有后台页面跳转
 */
@Controller
public class AdminPageController {

    @RequiresPermissions(value = {"admin:select"})
    @RequestMapping("/listCategory")
    public String editCategory(){
        return "admin/listCategory";
    }

    @RequiresPermissions(value = {"admin:select"})
    @RequestMapping("/admin_category_edit")
    public String admin_category_edit(){
        return "admin/editCategory";
    }


}
