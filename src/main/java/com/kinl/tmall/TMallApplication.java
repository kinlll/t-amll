package com.kinl.tmall;

import com.kinl.tmall.Utils.PriceUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@MapperScan(basePackages = "com.kinl.tmall.dao")
public class TMallApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return super.configure(application);
        return application.sources(TMallApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TMallApplication.class, args);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}


