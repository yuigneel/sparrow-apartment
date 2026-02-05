package com.yulgnier.web.admin.custom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j（springdoc-openapi）配置类
 * 用于配置API文档的基础信息和接口分组
 */
@Configuration
public class Knife4jConfig {

    /**
     * 配置API文档的全局基础信息（标题、版本、描述）
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("后台管理系统API")
                        .version("0.0.0-snapshot")
                        .contact(new Contact().name("yulgnier").url("https://github.com/yulgnier").email("yulgnier@gmail.com"))
                        .description("后台管理系统API"));
    }

    /**
     * 系统信息管理接口分组
     * 匹配路径：/admin/system/** 下的所有接口
     */
    @Bean
    public GroupedOpenApi systemAPI() {
        return GroupedOpenApi.builder()
                .group("系统信息管理")
                .pathsToMatch("/admin/system/**")
                .build();
    }

    /**
     * 后台登录管理接口分组
     * 匹配路径：/admin/login/** 和 /admin/info 接口
     */
    @Bean
    public GroupedOpenApi loginAPI() {
        return GroupedOpenApi.builder()
                .group("后台登录管理")
                .pathsToMatch(
                        "/admin/login/**",
                        "/admin/info"
                )
                .build();
    }

    /**
     * 公寓信息管理接口分组
     * 匹配公寓、房间、标签、设施等相关接口
     */
    @Bean
    public GroupedOpenApi apartmentAPI() {
        return GroupedOpenApi.builder()
                .group("公寓信息管理")
                .pathsToMatch(
                        "/admin/apartment/**",
                        "/admin/room/**",
                        "/admin/label/**",
                        "/admin/facility/**",
                        "/admin/fee/**",
                        "/admin/attr/**",
                        "/admin/payment/**",
                        "/admin/region/**",
                        "/admin/term/**",
                        "/admin/file/**"
                )
                .build();
    }

    /**
     * 租赁信息管理接口分组
     * 匹配预约、协议相关接口
     */
    @Bean
    public GroupedOpenApi leaseAPI() {
        return GroupedOpenApi.builder()
                .group("租赁信息管理")
                .pathsToMatch(
                        "/admin/appointment/**",
                        "/admin/agreement/**"
                )
                .build();
    }

    /**
     * 平台用户管理接口分组
     * 匹配用户相关接口
     */
    @Bean
    public GroupedOpenApi userAPI() {
        return GroupedOpenApi.builder()
                .group("平台用户管理")
                .pathsToMatch("/admin/user/**")
                .build();
    }
}