package com.kinl.tmall.service;

import com.kinl.tmall.Utils.Page;

import java.util.HashMap;

public interface UserService {

    String findNameById(Integer pid);

    Page pageQuery(HashMap<String, Object> map);
}
