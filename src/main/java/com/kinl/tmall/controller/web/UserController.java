package com.kinl.tmall.controller.web;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.Utils.ResultVOUtil;
import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/web")
public class UserController {

    @Autowired
    private UserService userService;

    @RequiresPermissions(value = {"admin:select"})
    @GetMapping("/users")
    public ResultVO pageUsers(@RequestParam(name = "start", required = false, defaultValue = "0") Integer pageIndex,
                              @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize){

        try {
            HashMap<String, Object> map = new HashMap<>();
            pageIndex = pageIndex < 0 ? 0 : pageIndex;
            map.put("pageIndex", pageIndex);
            map.put("pageSize", pageSize);

            Page page = userService.pageQuery(map);
            return ResultVOUtil.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(1, "查询用户失败");
        }

    }


}
