package com.kinl.tmall.service.impl;

import com.kinl.tmall.Utils.Page;
import com.kinl.tmall.VO.UserVO;
import com.kinl.tmall.dao.UserMapper;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import com.kinl.tmall.pojo.User;
import com.kinl.tmall.pojo.UserExample;
import com.kinl.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String findNameById(Integer pid) {
        return userMapper.findNameById(pid);
    }

    @Override
    public Page pageQuery(HashMap<String, Object> map) {
        Page page = new Page((Integer) map.get("pageSize"), (Integer) map.get("pageIndex"));
        Integer start = page.getStart();
        map.put("start", start);
        List<User> users = userMapper.pageQuery(map);
        page.setDatas(users);
        Integer count = userMapper.count();
        page.setRecord(count);
        page.setTotalPageCount(count);
        return page;
    }

    @Override
    public UserVO findVOById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        UserVO userVO = new UserVO();
        userVO.setName(user.getName());
        userVO.setAnonymousName(userVO.getAnonymousName());
        return userVO;
    }

    @Override
    public User findByName(String name) {
        User user = userMapper.findByName(name);
        return user;
    }

    @Override
    public User findById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            throw new AllException(ResultEnum.USER_NOT_EXIT);
        }
        return user;
    }

    @Override
    public Integer insert(User user) {
        int insert = userMapper.insert(user);
        if (insert == 0) {
            throw new AllException(ResultEnum.INSERT_USER_ERROR);
        }
        return insert;
    }
}
