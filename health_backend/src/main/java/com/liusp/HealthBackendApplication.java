package com.liusp;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
//添加对Dubbo的支持
@EnableDubbo
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class HealthBackendApplication {public static void main(String[] args) {
	SpringApplication.run(HealthBackendApplication.class, args);
}
}