package com.xzj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class MyUserDetailsService2 implements UserDetailsService {
    // @Autowired
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    public Map<String, com.liusp.pojo.User> map = new HashMap<>();

    //模拟数据库中的用户数据
    public void initData(){
        com.liusp.pojo.User user1 = new com.liusp.pojo.User();
        user1.setUsername("admin");
        //进行加密
        user1.setPassword(passwordEncoder.encode("12345"));
        System.out.println(user1.getPassword());


        com.liusp.pojo.User user2 = new com.liusp.pojo.User();
        user2.setUsername("xiaoming");
        //进行加密
        user2.setPassword(passwordEncoder.encode("12345"));
        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }
    /**
     * 根据用户名加载用户信息* @param username* @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        initData();
        System.out.println("username:" + username);
        com.liusp.pojo.User userInDb = map.get(username);
        //模拟根据用户名查询数据库
        if(userInDb == null){
            //根据用户名没有查询到用户
            return null;
        }
        //模拟数据库中的密码，后期需要查询数据库
        String passwordInDb = userInDb.getPassword();
        //授权，后期需要改为查询数据库动态获得用户拥有的权限和角色
        //将用户信息返回给框架
        //框架会进行密码比对(页面提交的密码和数据库中查询的密码进行比对)
        List<GrantedAuthority> list = new ArrayList<>();
        //为当前登录用户授权,后期需要改为从数据库查询当前用户对应的权限
        list.add(new SimpleGrantedAuthority("permission_A"));//授权
        list.add(new SimpleGrantedAuthority("permission_B"));
        if(username.equals("admin")){
            //list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//授予角色
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            list.add(new SimpleGrantedAuthority("add"));//授权
            list.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        UserDetails user = new User(username,passwordInDb,list);
        return user;
    }
}