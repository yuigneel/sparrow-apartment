package com.yulgnier.web.admin.custom.config;

import com.yulgnier.web.admin.custom.converter.StringToBaseEnumConverter;
import com.yulgnier.web.admin.custom.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final StringToBaseEnumConverter stringToBaseEnumConverter;
    private final AuthenticationInterceptor authenticationInterceptor;

    public WebMvcConfiguration(StringToBaseEnumConverter stringToBaseEnumConverter, AuthenticationInterceptor authenticationInterceptor) {
        this.stringToBaseEnumConverter = stringToBaseEnumConverter;
        this.authenticationInterceptor = authenticationInterceptor;
    }

    // 注册自定义转换器
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToBaseEnumConverter);
    }

    // 注册拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login/**")
        ;
    }
}
