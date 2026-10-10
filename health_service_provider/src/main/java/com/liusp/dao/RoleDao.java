package com.liusp.dao;

import com.liusp.pojo.Role;

import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(int id);
}
