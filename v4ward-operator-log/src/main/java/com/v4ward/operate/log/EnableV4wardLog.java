package com.v4ward.operate.log;

import com.v4ward.operate.log.interceptor.ModifyAspect;
import com.v4ward.operate.log.util.ModifyAutoConfig;
import com.v4ward.operate.log.util.SpringUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 是否引入详细记录
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({ModifyAutoConfig.class,ModifyAspect.class,SpringUtil.class})
public @interface EnableV4wardLog {
}
