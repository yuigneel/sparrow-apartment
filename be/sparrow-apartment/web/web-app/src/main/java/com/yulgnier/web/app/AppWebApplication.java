package com.yulgnier.web.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yulgnier.web.app.mapper")
public class AppWebApplication {
    public static void main(String[] args) {
        // 解决JMX MBean冲突问题
        System.setProperty("spring.application.admin.enabled", "false");
        SpringApplication.run(AppWebApplication.class, args);
    }
}


