package com.yulgnier.web.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
@SpringBootApplication(scanBasePackages = {"com.yulgnier.web.admin", "com.yulgnier.common.minio"})
@MapperScan("com.yulgnier.web.admin.mapper")
public class AdminWebApplication {
    // 项目入口main方法：启动Spring Boot应用
    public static void main(String[] args) {
        SpringApplication.run(AdminWebApplication.class, args);
        /*
        // 1. 启动Spring容器，获取上下文对象（关键：只有拿到上下文才能手动获取Bean）
        ConfigurableApplicationContext context = SpringApplication.run(AdminWebApplication.class, args);

        // 2. 打印启动成功提示
        System.out.println("========Spring Boot 启动成功！============");
        System.out.println("======== 开始测验MinIO工具类    ============");

        // 3. 从Spring上下文中手动获取MinioUtils Bean（main方法中唯一正确的方式）
        MinioUtils yulgnier = context.getBean(MinioUtils.class);

        // 4. 调用MinioUtils方法测试（示例：查询所有桶）
        try {
            // 测试查询所有Bucket
            System.out.println("MinIO所有存储桶：" + yulgnier.listAllBuckets());


            System.out.println("======== MinIO工具类测试完成    ============");
        } catch (Exception e) {
            System.err.println("MinIO测试失败：" + e.getMessage());
        }
         */
    }
}


