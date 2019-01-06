package com.v4ward.operate.log.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.v4ward.operate.log.demo.entity.Demo;

import java.io.Serializable;

/**
 * @author NieYinjun
 * @date 2019/1/4 15:02
 */
public interface DemoService extends IService<Demo> {
    /**
     * 插入demo数据
     * @param demo 实体参数
     */
    void insert(Demo demo);

    /**
     * 修改demo数据
     * @param demo  实体参数
     */
    void update(Demo demo);

    /**
     * 删除数据
     * @param id 主键ID
     */
    void delete(Serializable id);

}
