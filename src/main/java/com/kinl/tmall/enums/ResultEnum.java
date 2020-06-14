package com.kinl.tmall.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    USERNAME_ERROR(0, "用户名不能为空"),
    USERPWD_ERROR(1,"密码不正确"),
    LOGINTYPE_ERROR(2, "登陆类型不能为空"),
    UNKNOWNACCOUNT(3,"未知账户"),
    INCORRECTCREDENTIALS(4,"密码不正确"),
    LOCKEDACCOUNT(5,"账户已锁定"),
    EXCESSIVEATTEMPTS(6,"用户名或密码错误次数过多"),
    AUTHENTICATION(7,"用户名或密码不正确"),
    CATEGORYE_EMPTY(8,"字符串不能为空"),
    INSERT_ERROR(9,"插入分类失败"),
    CATEGORYEXIST(10, "分类已存在"),
    CATEGORYNOEXIST(11,"分类不存在"),
    PROPERTYEXIST(12,"属性已存在"),
    ADD_PROPERTY_ERROR(13,"添加属性失败"),
    PROPERTY_NOTEXIST(14,"属性不存在"),
    UPDATE_PROPERTY_ERROR(15,"属性更新失败"),
    DELETE_PROPERTY_ERROR(16,"删除属性失败"),
    PRODUCT_PAGE_ERROR(17,"产品加载失败"),
    ADD_PRODUCT_ERROR(18,"添加产品失败"),
    ADD_PRODUCTIMAGE_ERROR(19,"添加产品图片失败"),
    NOEXIST_PROPERTY(20,"不存在该属性"),
    PRODUCT_NOEXIST_PROPERTY(21,"产品不存在该属性"),
    PROPERTYVALUE_UPFATE_ERROR(22,"插入属性值失败"),
    PRODUCT_NOEXIST(23,"没有该产品"),
    PRODUCT_UPDATE_ERROR(24,"产品更新失败"),
    PRODUCT_EXIST_PROPERTYVALUE(25,"产品存在属性值"),
    ORDER_NOEXIST(26,"订单不存在"),
    ADD_ORDER_STATUS_ERROR(27,"改变订单状态失败"),
    FIND_PRODUCT_ERROR(28, "查找产品失败"),
    DELETE_PRODUCT_IMAGE_ERROR(29, "删除图片失败"),

    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
