package com.kinl.tmall.configuration;

import org.apache.shiro.authc.UsernamePasswordToken;


public class CustomizedToken extends UsernamePasswordToken {

    private String loginType;

    public CustomizedToken(String username, String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
