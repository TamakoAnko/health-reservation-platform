package com.liusp.dao;

import com.liusp.pojo.User;

public interface UserDao {
    public User findByUsername(String username);
}
