package com.aop.annotation;

import org.aspectj.lang.annotation.Aspect;

import java.lang.annotation.*;

/**
 * 自定义注解 拦截Controller
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
//注解表明这个注解应该被 javadoc工具记录. 默认情况下,javadoc是不包括注解的. 但如果声明注解时指定了 @Documented,则它会被 javadoc 之类的工具处理, 所以注解类型信息也会被包括在生成的文档中.
public @interface SystemControllerLog {
    String description() default "";
}
