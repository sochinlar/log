package com.soc.v4ward.log.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soc.v4ward.log.demo.entity.Demo;
import com.soc.v4ward.log.demo.mapper.DemoMapper;
import com.soc.v4ward.log.demo.service.DemoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author NieYinjun
 * @date 2019/1/4 15:18
 * @tag
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DemoServiceImpl extends ServiceImpl<DemoMapper,Demo> implements DemoService {
    @Resource
    private DemoMapper demoMapper;
    @Override
    public void insert(Demo demo) {
        demoMapper.insert(demo);
    }

    @Override
    public void update(Demo demo) {
        demoMapper.updateById(demo);
    }

    @Override
    public void delete(Serializable id) {
        demoMapper.deleteById(id);
    }
}
