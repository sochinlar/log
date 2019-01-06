package com.v4ward.operate.log.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author NieYinjun
 * @date 2019/1/4 15:03
 * @tag
 */
@Data
public class Demo implements Serializable {
    @TableId
    private Long id;
    private String name;
    private Integer count;
    @TableField(fill = FieldFill.INSERT)
    private String createEmp;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private String updateEmp;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer version;

}
