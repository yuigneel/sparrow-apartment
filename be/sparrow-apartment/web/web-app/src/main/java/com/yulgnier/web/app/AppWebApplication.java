package com.yulgnier.web.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.yulgnier.web.app",
        "com.yulgnier.common.exception", "com.yulgnier.common.mybatisplus","com.yulgnier.common.redis"})
@MapperScan("com.yulgnier.web.app.mapper")
@EnableAsync // 开启异步任务
public class AppWebApplication {
    public static void main(String[] args) {
        // 解决JMX MBean冲突问题
        System.setProperty("spring.application.admin.enabled", "false");
        SpringApplication.run(AppWebApplication.class, args);
    }
}


