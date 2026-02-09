package com.yulgnier.web.app.custom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j 接口文档配置类（基于 OpenAPI 3.0 规范）
 * 作用：配置接口文档的全局信息、分组规则，让 Knife4j 自动生成可视化的 API 文档
 * @Configuration 注解：标记这是一个 Spring 配置类，Spring 启动时会加载这个类中的配置
 */
@Configuration
public class Knife4jConfiguration {

    /**
     * 核心配置：定义 API 文档的全局基础信息（如标题、版本、联系人等）
     * @Bean 注解：将方法返回的 OpenAPI 对象注入到 Spring 容器中，供 Knife4j 使用
     * @return OpenAPI 对象：承载整个 API 文档的全局配置信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // 构建 OpenAPI 核心对象，链式调用配置各项属性（构建者模式）
        return new OpenAPI()
                // 配置文档的核心信息（标题、版本、描述等）
                .info(new Info()
                        // 接口文档的标题（会显示在 Knife4j 文档页面的顶部）
                        .title("APP接口")
                        // 接口版本号（建议和项目版本保持一致，如 1.0.0、0.0.0 等）
                        .version("0.0.0")
                        // 接口文档的描述（说明这个文档是做什么的，方便使用者理解）
                        .description("用户端APP接口")
                        // 配置接口文档的联系人信息（方便使用者有问题时联系开发人员）
                        .contact(new Contact()
                                .name("yulgnier")       // 联系人姓名/团队名
                                .url("https://github.com/yulgnier")  // 联系人/团队的网址（如GitHub、官网）
                                .email("yulgnier@gmail.com"))  // 联系人邮箱
                        // 服务条款地址（可选，标注API的使用规则/协议，无特殊要求可填示例地址）
                        .termsOfService("http://doc.xiaominfo.com")
                        // 配置API的许可证信息（声明项目遵循的开源协议/使用许可）
                        .license(new License()
                                .name("Apache 2.0")  // 许可证名称（如 Apache 2.0、MIT、私有协议等）
                                .url("http://doc.xiaominfo.com"))); // 许可证官方链接（建议填对应协议的官方地址）
    }

    /**
     * 接口分组配置：登录相关接口分组
     * 作用：将不同业务的接口分类展示，避免所有接口混在一起，提升文档可读性
     * @return GroupedOpenApi 对象：承载单个分组的配置规则
     */
    @Bean
    public GroupedOpenApi loginAPI() {
        // 构建分组对象，链式配置分组规则
        return GroupedOpenApi.builder()
                // 分组名称（会显示在 Knife4j 文档页面的分组下拉框中）
                .group("登录信息")
                // 匹配需要归到该分组的接口路径（支持通配符 ** 匹配多级路径）
                // 规则：/app/login/** 匹配 /app/login/ 下的所有接口；/app/info 精确匹配该接口
                .pathsToMatch("/app/login/**", "/app/info")
                // 构建并返回分组对象
                .build();
    }

    /**
     * 接口分组配置：个人信息相关接口分组
     */
    @Bean
    public GroupedOpenApi personAPI() {
        return GroupedOpenApi.builder()
                .group("个人信息")
                // 匹配个人信息模块的所有接口路径
                .pathsToMatch(
                        "/app/history/**",   // 历史记录相关接口
                        "/app/appointment/**", // 预约相关接口
                        "/app/agreement/**"    // 协议相关接口
                )
                .build();
    }

    /**
     * 接口分组配置：找房信息相关接口分组
     */
    @Bean
    public GroupedOpenApi lookForRoomAPI() {
        return GroupedOpenApi.builder()
                .group("找房信息")
                // 匹配找房模块的所有接口路径
                .pathsToMatch(
                        "/app/apartment/**", // 公寓相关接口
                        "/app/room/**",      // 房间相关接口
                        "/app/payment/**",   // 支付相关接口
                        "/app/region/**",    // 区域相关接口
                        "/app/term/**"       // 租期相关接口
                )
                .build();
    }
}