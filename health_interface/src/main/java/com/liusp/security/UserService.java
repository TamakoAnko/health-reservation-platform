package com.liusp.security;

import com.liusp.pojo.User;

public interface UserService {
    public User findByUsername(String username);

}
