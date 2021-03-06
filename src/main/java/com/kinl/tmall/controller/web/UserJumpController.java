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

    @GetMapping("/alipay")
    public String alipay(){
        return "fore/alipay";
    }

    @GetMapping("/payed")
    public String payed(){
        return "fore/payed";
    }

    @GetMapping("/bought")
    public String bought(){
        return "fore/bought";
    }

    @GetMapping("/confirmPay")
    public String confirmPay(){
        return "fore/confirmPay";
    }

    @GetMapping("/orderConfirmed")
    public String orderConfirmed(){
        return "fore/orderConfirmed";
    }

    @GetMapping("/review")
    public String review(){
        return "fore/review";
    }

    @GetMapping("/register")
    public String register(){
        return "fore/register";
    }

    @GetMapping("/registerSuccess")
    public String registerSuccess(){
        return "fore/registerSuccess";
    }

    @GetMapping("/search")
    public String search(){
        return "fore/search";
    }
}
