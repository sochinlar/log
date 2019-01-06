package com.v4ward.operate.log;


import java.lang.annotation.*;

/**
 * 用来注解不需要记录变动的字段
 * @author NieYinjun
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface IgnoreCompare {

 }
