package com.v4ward.operate.log;

import com.baomidou.mybatisplus.extension.service.IService;
import com.v4ward.operate.log.parser.DefaultContentParse;

import java.lang.annotation.*;

/**
 * 记录编辑详细信息的标注
 * @author lw
 * @date 2018-03-02
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface V4wardLog {
    /**
     * 操作的中文说明 可以直接调用ModifyName
     * @return
     */
    String name() default "";

    /**
     * 业务数据所在方法参数列表的位置，默认0，表示第一个参数
     * @return
     */
    int dataIndex() default 0;

    /**
     * 获取编辑信息的解析类，目前为使用id获取，复杂的解析需要自己实现，默认不填写
     * 则使用默认解析类
     * @return
     */
    Class parseClass() default DefaultContentParse.class;

    /**
     * 查询数据库所调用的class文件
     * @return
     */
    Class serviceClass() default IService.class;

    /**
     * 前台字段名称
     */
    String[] fieldName() default {"id"};
    /**
     * 具体业务操作名称
     */
    String businessName() default "";
}
