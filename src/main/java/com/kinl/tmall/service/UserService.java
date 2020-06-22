package com.kinl.tmall.service;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.VO.UserVO;
import com.kinl.tmall.pojo.User;

import java.util.HashMap;

public interface UserService {

    String findNameById(Integer pid);

    Page pageQuery(HashMap<String, Object> map);

    UserVO findVOById(Integer id);

    User findByName(String name);

    User findById(Integer id);
}
