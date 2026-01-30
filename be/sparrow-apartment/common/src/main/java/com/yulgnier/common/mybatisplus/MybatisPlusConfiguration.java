package com.yulgnier.common.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 插件配置类（分页插件核心配置）
 */
@Configuration
public class MybatisPlusConfiguration {


    /**
     * 注册 MyBatis-Plus 分页拦截器，开启分页功能
     * 核心作用：让 MyBatis-Plus 能够自动生成符合对应数据库的分页 SQL（如 MySQL 的 LIMIT 语句）
     * 说明：MyBatis-Plus 的分页功能不是默认开启的，需要手动配置分页拦截器才能生效
     * @return MybatisPlusInterceptor ：MyBatis-Plus 的核心拦截器容器，用于装载各种内部拦截器（分页、乐观锁等）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 1. 创建 MyBatis-Plus 拦截器容器（核心对象）
        // 解释：MybatisPlusInterceptor 是 MyBatis-Plus 提供的拦截器总容器，
        // 所有的 MyBatis-Plus 内部拦截器（分页、乐观锁、防全表更新删除等）都需要添加到这个容器中，才能生效
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 2. 向容器中添加「分页内部拦截器」（核心步骤，开启分页功能的关键）
        // 解释：addInnerInterceptor() 方法用于给总容器添加具体的功能拦截器，这里添加的是分页拦截器
        // 传入参数：PaginationInnerInterceptor 是分页功能的具体实现类，负责生成分页 SQL
        // DbType.MYSQL ：指定数据库类型为 MySQL，MyBatis-Plus 会根据不同的数据库类型，生成对应的分页语法（如 MySQL 用 LIMIT，Oracle 用 ROWNUM）
        // 若不指定数据库类型，MyBatis-Plus 会自动尝试识别，但手动指定更稳定，避免识别失败
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        // 3. 返回配置好的拦截器容器，注册到 Spring 容器中
        // 解释：Spring 会将这个拦截器容器注入到 MyBatis-Plus 中，启动时加载分页功能，后续查询即可使用分页
        return interceptor;
    }
}
