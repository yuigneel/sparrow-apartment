package com.yulgnier.web.admin;

import com.yulgnier.common.minio.MinioUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 麻雀租赁项目启动类
 * '@SpringBootApplication'：Spring Boot核心注解，包含3个关键功能：
 * 1. @Configuration：标记当前类为配置类
 * 2. @EnableAutoConfiguration：启用Spring Boot自动配置（自动配置数据源、Web容器等）
 * 3. @ComponentScan：自动扫描当前类所在包及子包下的@Component/@Service/@Controller等组件
 * '@MapperScan("com.yulgnier.web.admin.mapper")：自动扫描当前包及子包下的Mapper接口'
 */
@SpringBootApplication(scanBasePackages = {"com.yulgnier.web.admin", "com.yulgnier.common.minio",
        "com.yulgnier.common.exception", "com.yulgnier.common.mybatisplus"})
@MapperScan("com.yulgnier.web.admin.mapper")
public class AdminWebApplication {
    // 项目入口main方法：启动Spring Boot应用
    public static void main(String[] args) {
        // 1. 启动Spring Boot应用
        ConfigurableApplicationContext context = SpringApplication.run(AdminWebApplication.class, args);

/*
        // 2. 打印启动成功提示
        System.out.println("========Spring Boot 启动成功！============");
        System.out.println("======== 开始测验MinIO工具类    ============");

        // 3. 从Spring上下文中手动获取MinioUtils Bean（main方法中唯一正确的方式）
        MinioUtils minioUtils = context.getBean(MinioUtils.class); // 变量名语义化，更易理解

        // 4. 调用MinioUtils方法测试（示例：查询所有桶、设置桶权限）
        try {
            // 测试查询所有Bucket
            System.out.println("MinIO所有存储桶：" + minioUtils.listAllBuckets());
            //
            System.out.println("======== MinIO工具类测试完成    ============");
        } catch (Exception e) {
            System.err.println("MinIO测试失败：" + e.getMessage());
            e.printStackTrace(); // 打印完整异常栈，便于排查详细问题
        }
*/

    }
}