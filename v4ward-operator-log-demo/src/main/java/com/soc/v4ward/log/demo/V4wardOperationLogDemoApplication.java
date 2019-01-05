package com.soc.v4ward.log.demo;

import com.soc.v4ward.log.EnableLogAspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author NieYinjun
 * @date 2019/1/4 11:37
 * @tag
 */
@SpringBootApplication
@EnableLogAspect
@MapperScan("com.soc.v4ward.log.demo.mapper")
public class V4wardOperationLogDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(V4wardOperationLogDemoApplication.class,args);
    }
}
