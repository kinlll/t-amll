package com.kinl.tmall.dao;

import com.kinl.tmall.pojo.SysAdmin;

import java.util.List;

public interface SysAdminMapper {

    SysAdmin findByName(String name);

    List<String> findPermissionById(Integer id);

}
