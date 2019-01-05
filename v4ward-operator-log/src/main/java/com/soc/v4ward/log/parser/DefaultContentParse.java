package com.soc.v4ward.log.parser;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soc.v4ward.log.EnableOperateLog;
import com.soc.v4ward.log.util.SpringUtil;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 基础解析类
 * 单表编辑时可以直接使用id来查询
 * 如果为多表复杂逻辑，请自行编写具体实现类
 * @author lw
 * @date 2018-03-02
 */
public class DefaultContentParse implements ContentParser {
    @Override
    public Object getResult(Map<String,Object> feildValues, EnableOperateLog enableGameleyLog) {
        Assert.isTrue(feildValues.containsKey("id"),"未解析到id值，请检查前台传递参数是否正确");
        Object result= feildValues.get("id");
        Long id=0L;
        if(result instanceof String){
            id= Long.parseLong((String) result);

        }else if(result instanceof Integer){
            id= new Long((Integer)result);
        }else if(result instanceof Long){
            id = (Long) result;
        }
        IService service= null;
        Class cls=enableGameleyLog.serviceClass();
        service = (IService) SpringUtil.getBean(cls);

        return  service.getById(id);
    }


}
