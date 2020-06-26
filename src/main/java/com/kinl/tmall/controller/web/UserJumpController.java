package com.kinl.tmall.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 所有前端页面跳转
 */


@Controller
public class UserJumpController {

    @GetMapping("/home")
    public String home(){
        return "fore/home";
    }

    @GetMapping("/category")
    public String category(){
        return "fore/category";
    }

    @GetMapping("/product")
    public String product(){
        return "fore/product";
    }

    @GetMapping("/cart")
    public String cart(){
        return "fore/cart";
    }

    @GetMapping("/buy")
    public String buy(){
        return "fore/buy";
    }
}
