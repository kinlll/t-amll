package com.kinl.tmall.service.impl;

import com.kinl.tmall.dao.UserMapper;
import com.kinl.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String findNameById(Integer pid) {
        return userMapper.findNameById(pid);
    }
}
