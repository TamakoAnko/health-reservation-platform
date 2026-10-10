package com.liusp.dao;

import com.liusp.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(int roleId);
}
