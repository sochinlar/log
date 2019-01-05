package com.soc.v4ward.log.Interceptor;



import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.soc.v4ward.log.EnableGameleyLog;
import com.soc.v4ward.log.entity.OperateLog;
import com.soc.v4ward.log.parser.ContentParser;
import com.soc.v4ward.log.service.OperatelogService;
import com.soc.v4ward.log.util.BaseContextHandler;
import com.soc.v4ward.log.util.ClientUtil;
import com.soc.v4ward.log.util.ModifyName;
import com.soc.v4ward.log.util.ReflectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拦截@EnableGameleyLog注解的方法
 * 将具体修改存储到数据库中
 * Created by wwmxd on 2018/03/02.
 */
@Aspect
@Component
@ConditionalOnBean(OperatelogService.class)
@MapperScan("com.soc.v4ward.log.dao")
public class ModifyAspect {

    private final static Logger logger = LoggerFactory.getLogger(ModifyAspect.class);

    private OperateLog operateLog=new OperateLog();

    private Object oldObject;

    private Object newObject;

    private Map<String,Object> fieldValues=new HashMap<>();

    private Long idOfData;

    @Autowired
    private OperatelogService operatelogService;

    @Before("@annotation(enableGameleyLog)")
    public void doBefore(JoinPoint joinPoint, EnableGameleyLog enableGameleyLog){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object info=joinPoint.getArgs()[0];
        String[] fields=enableGameleyLog.feildName();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        operateLog.setUsername(BaseContextHandler.getName());
        operateLog.setModifyip(ClientUtil.getClientIp(request));
        operateLog.setModifydate(sdf.format(new Date()));
        String handelName=enableGameleyLog.handleName();
        if("".equals(handelName)){
            operateLog.setModifyobject(request.getRequestURI().toString());
        }else {
            operateLog.setModifyobject(handelName);
        }
        operateLog.setModifyname(enableGameleyLog.name());
        operateLog.setModifycontent("");
        if(ModifyName.UPDATE.equals(enableGameleyLog.name())||ModifyName.DELETE.equals(enableGameleyLog.name())){
            if(ModifyName.DELETE.equals(enableGameleyLog.name())){
                fieldValues.put("id",info);
            }else{
                for(String field:fields){
                    try{
                        Object result= ReflectionUtils.getFieldValue(info,field);
                        fieldValues.put(field,result);
                    }catch (Exception e){
                        logger.warn(e.getMessage());
                    }
                }
                fieldValues.put("id",ReflectionUtils.getFieldValue(info,ReflectionUtils.getPrimaryKeyName(info)));
            }
            try {
                ContentParser contentParser= (ContentParser) enableGameleyLog.parseclass().newInstance();
                oldObject=contentParser.getResult(fieldValues,enableGameleyLog);
            } catch (Exception e) {
                logger.error("service加载失败:",e);
            }
        }else if(ModifyName.SAVE.equals(enableGameleyLog.name())){
            idOfData = IdWorker.getId();
            String primaryKey = ReflectionUtils.getPrimaryKeyName(info);
            fieldValues.put("id",idOfData);
            ReflectionUtils.setFieldValue(info,primaryKey,idOfData);
        }else{
            if(ModifyName.UPDATE.equals(enableGameleyLog.name())){
                logger.error("id查询失败，无法记录日志");
            }
        }
    }

    @AfterReturning(pointcut = "@annotation(enableGameleyLog)",returning = "object")
    public void doAfterReturning(Object object, EnableGameleyLog enableGameleyLog){
        ContentParser contentParser= null;
        try {
            contentParser = (ContentParser) enableGameleyLog.parseclass().newInstance();
        } catch (Exception e) {
            logger.error("service加载失败:",e);
        }
        if(ModifyName.UPDATE.equals(enableGameleyLog.name())){
            newObject=contentParser.getResult(fieldValues,enableGameleyLog);
            try {
                List<Map<String ,Object>> changelist= ReflectionUtils.compareTwoClass(oldObject,newObject);
                StringBuilder str=new StringBuilder();
                for(Map<String,Object> map:changelist){
                    str.append("【"+map.get("name")+"】从【"+map.get("old")+"】改为了【"+map.get("new")+"】;\n");
                }
                operateLog.setModifycontent(str.toString());

            } catch (Exception e) {
                logger.error("比较异常",e);
            }
        }else if(ModifyName.SAVE.equals(enableGameleyLog.name())){
            newObject = contentParser.getResult(fieldValues,enableGameleyLog);
            operateLog.setModifycontent(JSON.toJSONString(newObject));
        }else if(ModifyName.DELETE.equals(enableGameleyLog.name())){
            operateLog.setModifycontent(JSON.toJSONString(oldObject));
        }
        operatelogService.save(operateLog);

    }


}
