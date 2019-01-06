package com.v4ward.operate.log.demo.controller;

import com.v4ward.core.web.http.RestResponseEntity;
import com.v4ward.operate.log.V4wardLog;
import com.v4ward.operate.log.demo.entity.Demo;
import com.v4ward.operate.log.demo.service.DemoService;
import com.v4ward.operate.log.util.ModifyName;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author NieYinjun
 * @date 2019/1/4 14:56
 * @tag
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private DemoService demoService;

    @PostMapping
    @V4wardLog(name = ModifyName.SAVE,serviceClass = DemoService.class)
    public Object addDemo(@RequestBody Demo demo){
        demoService.insert(demo);
        return RestResponseEntity.ok("新增成功！");
    }
    @PutMapping
    @V4wardLog(name = ModifyName.UPDATE,serviceClass = DemoService.class)
    public Object updateDemo(@RequestBody Demo demo){
        demoService.update(demo);
        return RestResponseEntity.ok("修改成功！");
    }
    @DeleteMapping("/{id}")
    @V4wardLog(name = ModifyName.DELETE,serviceClass = DemoService.class)
    public Object delDemo(@PathVariable("id") String id){
        demoService.delete(id);
        return RestResponseEntity.ok("删除成功！");
    }

}
