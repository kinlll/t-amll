package com.kinl.tmall.controller;

        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("test")
    public String test(){

        //int i = 10 / 0;

        return "hello";
    }
}
