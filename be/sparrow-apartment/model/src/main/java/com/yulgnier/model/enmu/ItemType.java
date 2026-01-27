package com.yulgnier.model.enmu;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ItemType implements BaseEnum {

    APARTMENT(1, "公寓"),

    ROOM(2, "房间");


    @EnumValue //MybatisPlus 枚举注解, 数据库存储该枚举的数值
    @JsonValue  // Jackson 枚举注解, 获取枚举时返回该枚举的数值
    private Integer code;
    private String name;

    @Override
    public Integer getCode() {
        return this.code;
    }


    @Override
    public String getName() {
        return name;
    }

    ItemType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
