package com.yulgnier.model.enmu;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AppointmentStatus implements BaseEnum {


    WAITING(1, "待看房"),

    CANCELED(2, "已取消"),

    VIEWED(3, "已看房");

    /**
     * 核心注解作用说明：
     * 1. @EnumValue (MyBatis Plus)：
     *    - 存库：将枚举的code值（1/2/3）存入数据库字段
     *    - 查库：将数据库数字值映射为对应枚举实例（如 1→WAITING）
     * 2. @JsonValue (Jackson)：
     *    - 序列化：返回前端时仅输出code值（1/2/3），不返回枚举名/完整对象
     */
    @EnumValue
    @JsonValue
    private Integer code;


    private String name;

    AppointmentStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
