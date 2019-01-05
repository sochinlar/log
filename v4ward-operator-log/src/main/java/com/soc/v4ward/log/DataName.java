package com.soc.v4ward.log;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DataName {
    /**
     * 字段名称
     * @return
     */
    String name() default "";

 }
