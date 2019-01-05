package com.soc.v4ward.log.util;

import com.soc.v4ward.log.service.OperatelogService;
import com.soc.v4ward.log.service.impl.OperatelogServiceImpl;
import org.springframework.context.annotation.Bean;

public class ModifyAutoConfig {

    @Bean
    public OperatelogService operatelogService(){
        return new OperatelogServiceImpl();
    }



}
