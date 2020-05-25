package com.kinl.tmall.enums;

import lombok.Getter;

@Getter
public enum LoginType {
    USER("User"),
    ADMIN("Admin"),
    ;

    private String type;

    LoginType(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return "LoginType{" +
                "type='" + type + '\'' +
                '}';
    }
}
