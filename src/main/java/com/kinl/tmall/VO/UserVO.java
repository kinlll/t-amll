package com.kinl.tmall.VO;

import lombok.Data;

@Data
public class UserVO {
    private String name;

    private String password;

    private String loginType;

    private String anonymousName;

    public String getAnonymousName() {
        if (name.length() > 2){
            char[] chars = name.toCharArray();
            for (int i = 1; i < chars.length-1; i++){
                chars[i] = '*';
            }
            this.anonymousName = new String(chars);
        }else if (name.length() > 1){
            this.anonymousName = anonymousName.substring(0, 1) + "*";
        }else if (name.length() > 0){
            this.anonymousName = "*";
        }
        return anonymousName;
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }
}
