package com.v4ward.operate.log.util;

import com.v4ward.operate.log.service.SysOperateLogService;
import com.v4ward.operate.log.service.impl.SysOperateLogServiceImpl;
import org.springframework.context.annotation.Bean;
/**
 * @author NieYinjun 2019-01-05 13:25:20
 */
public class ModifyAutoConfig {

    @Bean
    public SysOperateLogService sysOperateLogService(){
        return new SysOperateLogServiceImpl();
    }



}
