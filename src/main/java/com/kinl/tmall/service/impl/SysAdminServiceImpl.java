package com.kinl.tmall.service.impl;

import com.kinl.tmall.dao.SysAdminMapper;
import com.kinl.tmall.pojo.SysAdmin;
import com.kinl.tmall.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAdminServiceImpl implements SysAdminService {

    @Autowired
    private SysAdminMapper sysAdminMapper;

    @Override
    public SysAdmin findByName(String name) {
        return sysAdminMapper.findByName(name);
    }

    @Override
    public List<String> findPermissionById(Integer id) {
        return sysAdminMapper.findPermissionById(id);
    }
}
