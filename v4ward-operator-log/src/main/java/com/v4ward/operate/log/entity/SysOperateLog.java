package com.v4ward.operate.log.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author NieYinjun
 * @date 2019/1/5 11:04
 * @tag
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_operate_log")
public class SysOperateLog extends Model {

    /**
     *  主键ID
     */
    private Long id;
    /**
     * 操作数据的主键ID，可用于追踪轨迹
     */
    private Object dataId;
    /**
     * 操作类型：新增，修改，删除
     */
    private String operateType;
    /**
     *业务名称或接口URI
     */
    private String businessName;
    /**
     *操作数据
     */
    private String operateData;
    /**
     *操作用户的IP
     */
    private String operateIp;
    /**
     *操作用户名
     */
    @TableField(fill = FieldFill.INSERT)
    private Long userId;
    /**
     *操作用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String createEmp;
    /**
     *操作时间
     */
    private LocalDateTime createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
