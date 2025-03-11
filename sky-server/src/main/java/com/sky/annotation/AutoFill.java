package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoFill
 * Description:自定义注解，用于公共字段的填充
 *
 * @Author 乒乓界李大帅
 * @Create 2025/2/20 18:52
 */
@Target(ElementType.METHOD)// 作用在方法上
@Retention(RetentionPolicy.RUNTIME)// 运行时
public @interface AutoFill {
    // 操作类型: insert, update
    OperationType value();
}
