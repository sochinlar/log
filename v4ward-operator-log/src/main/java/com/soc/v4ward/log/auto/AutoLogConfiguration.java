package com.soc.v4ward.log.auto;

import com.soc.v4ward.log.interceptor.ModifyAspect;
import com.soc.v4ward.log.util.SpringUtil;
import com.v4ward.core.mybatis.configuration.MyBatisPlusConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author NieYinjun
 * @date 2019/1/4 14:12
 * @tag
 */
@Configuration
@AutoConfigureAfter({MyBatisPlusConfiguration.class})
@ConditionalOnClass(ModifyAspect.class)
@Import({ModifyAspect.class,SpringUtil.class})
public class AutoLogConfiguration {
}
