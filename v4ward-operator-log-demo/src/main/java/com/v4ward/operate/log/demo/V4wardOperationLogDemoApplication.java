package com.v4ward.operate.log.demo;

import com.v4ward.operate.log.EnableV4wardLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author NieYinjun
 * @date 2019/1/4 11:37
 * @tag
 */
@SpringBootApplication
@EnableV4wardLog
@MapperScan("com.v4ward.operate.log.demo.mapper")
public class V4wardOperationLogDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(V4wardOperationLogDemoApplication.class,args);
    }
}
