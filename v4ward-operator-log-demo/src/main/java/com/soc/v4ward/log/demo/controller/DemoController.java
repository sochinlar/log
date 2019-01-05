package com.soc.v4ward.log.demo.controller;
import com.soc.v4ward.log.EnableGameleyLog;
import com.soc.v4ward.log.demo.entity.Demo;
import com.soc.v4ward.log.demo.service.DemoService;
import com.soc.v4ward.log.util.ModifyName;
import com.v4ward.core.web.http.RestResponseEntity;
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
    @EnableGameleyLog(name = ModifyName.SAVE,serviceclass = DemoService.class)
    public Object addDemo(@RequestBody Demo demo){
        demoService.insert(demo);
        return RestResponseEntity.ok("新增成功！");
    }
    @PutMapping
    @EnableGameleyLog(name = ModifyName.UPDATE,serviceclass = DemoService.class)
    public Object updateDemo(@RequestBody Demo demo){
        demoService.update(demo);
        return RestResponseEntity.ok("修改成功！");
    }
    @DeleteMapping("/{id}")
    @EnableGameleyLog(name = ModifyName.DELETE,serviceclass = DemoService.class)
    public Object delDemo(@PathVariable("id") String id){
        demoService.delete(id);
        return RestResponseEntity.ok("删除成功！");
    }

}
