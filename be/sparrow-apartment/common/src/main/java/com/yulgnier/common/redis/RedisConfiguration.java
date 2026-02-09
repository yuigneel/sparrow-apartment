package com.yulgnier.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;


/**
 * Redis 核心配置类
 * 作用：自定义 RedisTemplate 实例，解决默认 RedisTemplate 序列化乱码、类型不匹配等问题
 * 注解说明：@Configuration 标记这是一个配置类，Spring 启动时会扫描并加载这个类中的 Bean 定义
 */
@Configuration
public class RedisConfiguration {
    /**
     * 自定义 RedisTemplate Bean
     * RedisTemplate 是 Spring Data Redis 提供的核心操作类，用于和 Redis 服务器交互（存/取/删数据）
     *
     * @param redisConnectionFactory Redis 连接工厂（由 Spring 自动注入，无需手动创建，底层封装了 Redis 连接池、地址、端口等配置）
     * @return 配置好序列化规则的 RedisTemplate 实例，供项目中注入使用
     */
    @Bean // 注解说明：将方法返回的对象交给 Spring 容器管理，全局唯一，可通过 @Autowired 注入使用
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 1. 创建 RedisTemplate 核心对象，泛型 <String, Object> 表示：Key 是字符串类型，Value 是任意对象类型
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 2. 设置 Redis 连接工厂（核心步骤：让 RedisTemplate 知道连接哪个 Redis 服务器）
        template.setConnectionFactory(redisConnectionFactory);

        // 3. 配置 Key 的序列化器（解决 Key 乱码问题）
        // RedisSerializer.string() 等价于 StringRedisSerializer，专门序列化字符串，兼容 Redis 原生格式
        // 如果不配置，默认用 JdkSerializationRedisSerializer，会导致 Key 出现乱码（如 \xAC\xED\x00\x05t\x00\x03key）
        template.setKeySerializer(RedisSerializer.string());

        // 4. 配置 Value 的序列化器（解决 Value 乱码问题）
        // RedisSerializer.java() 等价于 JdkSerializationRedisSerializer，支持序列化任意 Java 对象（需实现 Serializable 接口）
        // 可选方案：如果想让 Value 以 JSON 格式存储（更易读），可替换为 RedisSerializer.json()
        template.setValueSerializer(RedisSerializer.java());

        // 5. 返回配置完成的 RedisTemplate 实例，交给 Spring 容器
        return template;
    }
}