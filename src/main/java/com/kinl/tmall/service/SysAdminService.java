package com.kinl.tmall.service;

import com.kinl.tmall.pojo.SysAdmin;

import java.util.List;

public interface SysAdminService {

    SysAdmin findByName(String name);

    List<String> findPermissionById(Integer id);
}
