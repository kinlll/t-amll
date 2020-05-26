package com.kinl.tmall.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserJumpController {

    @GetMapping("/home")
    public String home(){

        return "fore/home";
    }
}
