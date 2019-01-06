package com.v4ward.operate.log.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.v4ward.operate.log.V4wardLog;
import com.v4ward.operate.log.dto.OperateDataDTO;
import com.v4ward.operate.log.dto.UpdateDTO;
import com.v4ward.operate.log.entity.SysOperateLog;
import com.v4ward.operate.log.parser.ContentParser;
import com.v4ward.operate.log.service.SysOperateLogService;
import com.v4ward.operate.log.util.BaseContextHandler;
import com.v4ward.operate.log.util.ClientUtil;
import com.v4ward.operate.log.util.ModifyName;
import com.v4ward.operate.log.util.ReflectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@MapperScan("com.v4ward.operate.log.mapper")
public class ModifyAspect {

    private final static Logger logger = LoggerFactory.getLogger(ModifyAspect.class);

    private SysOperateLog operateLog=new SysOperateLog();

    private Object oldObject;

    private Object newObject;

    private Map<String,Object> fieldValues=new HashMap<>();

    private Long idOfData;

    @Resource
    private SysOperateLogService sysOperateLogService;

    @Before("@annotation(v4wardLog)")
    public void doBefore(JoinPoint joinPoint, V4wardLog v4wardLog){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        int dataIndex = v4wardLog.dataIndex();
        Object info=joinPoint.getArgs()[dataIndex];
        String[] fields= v4wardLog.fieldName();
        operateLog.setUserId(BaseContextHandler.getUserId());
        operateLog.setOperateIp(ClientUtil.getClientIp(request));
        operateLog.setId(IdWorker.getId());
        String handelName= v4wardLog.businessName();
        if("".equals(handelName)){
            operateLog.setBusinessName(request.getRequestURI());
        }else {
            operateLog.setBusinessName(handelName);
        }
        operateLog.setOperateType(v4wardLog.name());
        operateLog.setOperateData("");
        if(ModifyName.UPDATE.equals(v4wardLog.name())||ModifyName.DELETE.equals(v4wardLog.name())){
            if(ModifyName.DELETE.equals(v4wardLog.name())){
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
                ContentParser contentParser= (ContentParser) v4wardLog.parseClass().newInstance();
                oldObject=contentParser.getResult(fieldValues, v4wardLog);
            } catch (Exception e) {
                logger.error("service加载失败:",e);
            }
        }else if(ModifyName.SAVE.equals(v4wardLog.name())){
            idOfData = IdWorker.getId();
            String primaryKey = ReflectionUtils.getPrimaryKeyName(info);
            fieldValues.put("id",idOfData);
            ReflectionUtils.setFieldValue(info,primaryKey,idOfData);
        }else{
            if(ModifyName.UPDATE.equals(v4wardLog.name())){
                logger.error("id查询失败，无法记录日志");
            }
        }
    }

    @AfterReturning(pointcut = "@annotation(v4wardLog)",returning = "object")
    public void doAfterReturning(Object object, V4wardLog v4wardLog){
        ContentParser contentParser= null;
        try {
            contentParser = (ContentParser) v4wardLog.parseClass().newInstance();
        } catch (Exception e) {
            logger.error("service加载失败:",e);
        }
        if(ModifyName.UPDATE.equals(v4wardLog.name())){
            newObject=contentParser.getResult(fieldValues, v4wardLog);
            try {
                List<UpdateDTO> changelist= ReflectionUtils.compareTwoClass(oldObject,newObject);
                OperateDataDTO operateDataDTO = new OperateDataDTO();
                operateDataDTO.setId(fieldValues.get("id"));
                operateDataDTO.setUpdates(changelist);
                operateLog.setOperateData(operateDataDTO.toString());

            } catch (Exception e) {
                logger.error("比较异常",e);
            }
        }else if(ModifyName.SAVE.equals(v4wardLog.name())){
            newObject = contentParser.getResult(fieldValues, v4wardLog);
            if(newObject == null){
                logger.warn("没找到新增数据，忽略操作日志记录！");
                return;
            }
            operateLog.setOperateData(JSON.toJSONString(newObject));
        }else if(ModifyName.DELETE.equals(v4wardLog.name())){
            if(oldObject == null){
                logger.warn("没找到要删除的数据，忽略操作日志记录！");
                return;
            }
            operateLog.setOperateData(JSON.toJSONString(oldObject));
        }
        operateLog.setDataId(fieldValues.get("id"));
        sysOperateLogService.save(operateLog);

    }


}
