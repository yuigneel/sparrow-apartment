package com.yulgnier.common.exception;

import com.yulgnier.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常，继承RuntimeException，用于统一处理业务层异常场景
 * '@EqualsAndHashCode(callSuper = true) 注解核心解释：'
 * 1. 注解作用：控制Lombok生成equals()和hashCode()方法时，是否纳入父类的属性参与计算
 * 2. 必须添加的原因：
 * - 当前异常类通过@Data注解自动生成equals()、hashCode()，Lombok默认仅基于【当前类自定义字段】生成逻辑
 * - 本类继承自RuntimeException，而RuntimeException的父类Throwable包含核心属性：message（异常信息）、cause（异常根源）、stackTrace（堆栈信息）等
 * - 异常的相等性判断，核心依赖父类的message、cause等关键属性，仅用子类字段判断会丢失异常核心特征
 * 3. 不添加的潜在问题：
 * - 相等性判断失效：两个message、cause完全相同的异常实例，equals()会返回false，违背异常相等性的业务认知
 * - 集合使用异常：将异常存入HashMap、HashSet等基于hashCode()和equals()判重的集合时，会出现“重复存储”“查找不到”的问题
 * - 测试/日志校验失败：单元测试中断言异常属性、日志系统中异常去重统计时，会出现逻辑错误，影响问题排查
 * - 违背Java规范：Java中equals()和hashCode()的设计原则要求，子类重写时需保证与父类逻辑兼容，异常类更需遵循此规范
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaseException extends RuntimeException {
    private Integer code;

    public LeaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public LeaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
