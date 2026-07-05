package com.student.annotation;

import java.lang.annotation.*;

/**
 * 忽略Token验证注解
 * 标记在Controller方法上，表示该接口不需要登录验证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {
}
