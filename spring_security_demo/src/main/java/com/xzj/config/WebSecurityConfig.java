package com.xzj.config;

import com.xzj.service.MyUserDetailsService;
import com.xzj.service.MyUserDetailsService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static java.lang.Compiler.disable;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //访问数据库权限(无加密)
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    //访问数据库权限(加密)
    @Autowired
    private MyUserDetailsService2 myUserDetailsService2;

    //密码加密方式
    @Bean
    PasswordEncoder passwordEncoder(){
        //无加密
        //return NoOpPasswordEncoder.getInstance();

        //加密
        return new BCryptPasswordEncoder();
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws
            Exception {
        auth.userDetailsService(myUserDetailsService2).passwordEncoder(passwordEncoder());
    }


    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //所有用户可以访问静态资源以及访问"/pages/login.html"和"/pages/hello.html"
                .antMatchers("/static/**", "/pages/login.html", "/pages/hello.html").permitAll()
                //管理员角色可以访问"/emp/data"
                .antMatchers("/emp/data").hasAuthority("ROLE_ADMIN")
                //用户和管理员都可以访问"/emp"，但用户不可以访问上面的"/emp/data"
                .antMatchers("/emp/*").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                //以"/emp/data"开始的URL，必须拥有 "ROLE_ADMIN" 角色权限,这里用hasRole不需要写"ROLE_"前缀；
                //.antMatchers("/emp/data").hasRole("ADMIN")
                //以"/emp"开始的URL，必须拥有 "ROLE_ADMIN" 角色权限和 "ROLE_USER"角色,这里不需要写"ROLE_"前缀；
                //.antMatchers("/emp/*").access("hasRole('ADMIN') andhasRole('USER')")
                //前面没有匹配上的请求，全部需要认证；

                .anyRequest()
                .authenticated()

                .and()
                //指定登录界面，并且设置为所有人都能访问；
                .formLogin()
                .loginPage("/hello")
                //登陆请求处理接口
                .loginProcessingUrl("/login")
                //用户名文本框 name属性
                .usernameParameter("username")
                //密码文本框 name属性
                .passwordParameter("password")
                //如果登录成功会跳转到"/hello".defaultSuccessUrl("/index")//登录成功处理
                .successHandler((req, resp, auth) -> {
                })
                //如果登录失败会跳转到"/hello".failureForwardUrl("/hello")//登录失败处理
                .failureHandler((req, resp, e) -> {
                })
                .permitAll()

                .and()
                //登出配置
                .logout()
                .logoutUrl("/logout") //指定登出的地址，默认是"/logout"
                .logoutSuccessUrl("/hello") //登出后的跳转地址
                .clearAuthentication(true) //清楚身份信息
                .invalidateHttpSession(true) //session 失效，默认为true
                .deleteCookies("usernameCookie","urlCookie") //在登出同时清除

                .and()
                //自定义403页面
                .exceptionHandling()
                .accessDeniedPage("/403")

                .and()
                .csrf()         //关闭csrf防护
                .disable();
    }

}


