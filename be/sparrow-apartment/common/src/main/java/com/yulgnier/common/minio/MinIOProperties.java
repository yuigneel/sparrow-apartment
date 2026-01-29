package com.yulgnier.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIOProperties {

    // 基础认证配置
    private String accessKey;    // MinIO账号
    private String secretKey;    // MinIO密码
    private String bucketName;   // 默认存储桶名称

    // 补充yml里的配置字段（必须加，否则配置类拿不到这些值）
    private String endpoint;     // MinIO服务API地址（http://127.0.0.1:9000）
    private Boolean secure;      // 是否启用HTTPS
    private Boolean pathStyleAccess; // 是否启用路径样式访问


    /**
     // 临时调试方法：Bean初始化后执行的方法，验证是否被Spring扫描到
     @PostConstruct public void init() {
     // 打印关键信息：确认Bean被加载，同时可查看配置是否注入成功
     System.out.println("======= MinIOProperties 被Spring扫描并初始化 =======");
     System.out.println("accessKey = " + this.accessKey); // 打印配置值（yml里配了就有值，没配则为null）
     System.out.println("secretKey = " + this.secretKey);
     System.out.println("bucketName = " + this.bucketName);
     System.out.println("==================================================");
     }
     */
}
