package com.xzj.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

@RestController
public class UserController {
    @GetMapping(value = "/hello")
    public ModelAndView hello(){
        return new ModelAndView("/login.html");

    }

    @GetMapping(value = "/index")
    public ModelAndView index(){
        return new ModelAndView("/index.html");
    }

    @GetMapping(value = "/403")
    public ModelAndView go403(){
        return new ModelAndView("/403.html");
    }

    @GetMapping("/test1")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public String test() {
        return "hello";
    }

    @GetMapping("/test2")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') AND hasAuthority('ROLE_USER')")
    public String test2() {return "hello2";
    }

    @PostAuthorize("returnObject %2 == 0")
    @GetMapping(value = "/test3")
    public int test3(){
        int i=new Random().nextInt(10);
        System.out.println(i);
        return i;
    }


}
