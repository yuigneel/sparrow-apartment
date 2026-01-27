package com.yulgnier.web.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * 麻雀租赁项目启动类
 * '@SpringBootApplication'：Spring Boot核心注解，包含3个关键功能：
 * 1. @Configuration：标记当前类为配置类
 * 2. @EnableAutoConfiguration：启用Spring Boot自动配置（自动配置数据源、Web容器等）
 * 3. @ComponentScan：自动扫描当前类所在包及子包下的@Component/@Service/@Controller等组件
 */

// 若使用MyBatis-Plus，需添加@MapperScan（可选，根据你的项目依赖）
// import org.mybatis.spring.annotation.MapperScan;
// 若用MyBatis-Plus，需添加@MapperScan指定Mapper接口所在包（比如：@MapperScan("com.sparrow.rental.mapper")）
@SpringBootApplication
@MapperScan("com.yulgnier.web.admin.mapper")
public class AdminWebApplication {
    // 项目入口main方法：启动Spring Boot应用
    public static void main(String[] args) {
        // 启动Spring容器，加载配置并初始化所有组件
        SpringApplication.run(AdminWebApplication.class, args);
    }
}


