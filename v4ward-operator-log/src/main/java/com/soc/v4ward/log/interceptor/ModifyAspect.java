package com.soc.v4ward.log.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.soc.v4ward.log.EnableOperateLog;
import com.soc.v4ward.log.dto.OperateDataDTO;
import com.soc.v4ward.log.dto.UpdateDTO;
import com.soc.v4ward.log.entity.SysOperateLog;
import com.soc.v4ward.log.parser.ContentParser;
import com.soc.v4ward.log.service.SysOperateLogService;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拦截@EnableOperateLog注解的方法
 * 将具体修改存储到数据库中
 * @author  2018/03/02.
 */
@Aspect
@Component
@ConditionalOnBean(SysOperateLogService.class)
@MapperScan("com.soc.v4ward.log.mapper")
public class ModifyAspect {

    private final static Logger logger = LoggerFactory.getLogger(ModifyAspect.class);

    private SysOperateLog operateLog=new SysOperateLog();

    private Object oldObject;

    private Object newObject;

    private Map<String,Object> fieldValues=new HashMap<>();

    private Long idOfData;

    @Resource
    private SysOperateLogService sysOperateLogService;

    @Before("@annotation(enableOperateLog)")
    public void doBefore(JoinPoint joinPoint, EnableOperateLog enableOperateLog){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object info=joinPoint.getArgs()[0];
        String[] fields=enableOperateLog.fieldName();
        operateLog.setUsername(BaseContextHandler.getName());
        operateLog.setOperateIp(ClientUtil.getClientIp(request));
        operateLog.setCreateTime(LocalDateTime.now());
        operateLog.setId(IdWorker.getId());
        String handelName=enableOperateLog.businessName();
        if("".equals(handelName)){
            operateLog.setBusinessName(request.getRequestURI());
        }else {
            operateLog.setBusinessName(handelName);
        }
        operateLog.setOperateType(enableOperateLog.name());
        operateLog.setOperateData("");
        if(ModifyName.UPDATE.equals(enableOperateLog.name())||ModifyName.DELETE.equals(enableOperateLog.name())){
            if(ModifyName.DELETE.equals(enableOperateLog.name())){
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
                ContentParser contentParser= (ContentParser) enableOperateLog.parseClass().newInstance();
                oldObject=contentParser.getResult(fieldValues,enableOperateLog);
            } catch (Exception e) {
                logger.error("service加载失败:",e);
            }
        }else if(ModifyName.SAVE.equals(enableOperateLog.name())){
            idOfData = IdWorker.getId();
            String primaryKey = ReflectionUtils.getPrimaryKeyName(info);
            fieldValues.put("id",idOfData);
            ReflectionUtils.setFieldValue(info,primaryKey,idOfData);
        }else{
            if(ModifyName.UPDATE.equals(enableOperateLog.name())){
                logger.error("id查询失败，无法记录日志");
            }
        }
    }

    @AfterReturning(pointcut = "@annotation(enableOperateLog)",returning = "object")
    public void doAfterReturning(Object object, EnableOperateLog enableOperateLog){
        ContentParser contentParser= null;
        try {
            contentParser = (ContentParser) enableOperateLog.parseClass().newInstance();
        } catch (Exception e) {
            logger.error("service加载失败:",e);
        }
        if(ModifyName.UPDATE.equals(enableOperateLog.name())){
            newObject=contentParser.getResult(fieldValues,enableOperateLog);
            try {
                List<UpdateDTO> changelist= ReflectionUtils.compareTwoClass(oldObject,newObject);
                OperateDataDTO operateDataDTO = new OperateDataDTO();
                operateDataDTO.setId(fieldValues.get("id"));
                operateDataDTO.setUpdates(changelist);
                operateLog.setOperateData(operateDataDTO.toString());

            } catch (Exception e) {
                logger.error("比较异常",e);
            }
        }else if(ModifyName.SAVE.equals(enableOperateLog.name())){
            newObject = contentParser.getResult(fieldValues,enableOperateLog);
            operateLog.setOperateData(JSON.toJSONString(newObject));
        }else if(ModifyName.DELETE.equals(enableOperateLog.name())){
            operateLog.setOperateData(JSON.toJSONString(oldObject));
        }
        sysOperateLogService.save(operateLog);

    }


}
