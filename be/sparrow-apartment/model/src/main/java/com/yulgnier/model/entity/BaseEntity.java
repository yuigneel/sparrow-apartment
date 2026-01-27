package com.yulgnier.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonIgnore // 忽略此字段
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonIgnore
    @Schema(description = "更新时间")
    private Date updateTime;

    @JsonIgnore
    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic(value = "0", delval = "1")      //默认0为否删除，1为删除
    private Byte isDeleted;

}