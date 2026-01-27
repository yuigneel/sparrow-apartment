package com.yulgnier.web.admin.custom.config;

import com.yulgnier.web.admin.custom.converter.StringToBaseEnumConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private StringToBaseEnumConverter stringToBaseEnumConverter;

    // 注册自定义转换器
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToBaseEnumConverter);
    }
}
