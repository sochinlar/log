package com.soc.v4ward.log.util;

import com.soc.v4ward.log.service.SysOperateLogService;
import com.soc.v4ward.log.service.impl.SysOperateLogServiceImpl;
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
