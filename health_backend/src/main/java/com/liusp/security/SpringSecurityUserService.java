package com.liusp.security;

import com.liusp.pojo.Permission;
import com.liusp.pojo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Reference;

import java.util.*;


@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    //根据用户名查询用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //远程调用用户服务，根据用户名查询用户信息
        com.liusp.pojo.User user = userService.findByUsername(username);
        if(user == null){
        //用户名不存在
            throw new UsernameNotFoundException("用户名不存在: " + username);
        }
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for(Role role : roles){
            //授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for(Permission permission : permissions){
                //授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        UserDetails userDetails = new User(username,user.getPassword(),list);
        return userDetails;
    }
}













