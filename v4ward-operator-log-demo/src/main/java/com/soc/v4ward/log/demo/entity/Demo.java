package com.soc.v4ward.log.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.soc.v4ward.log.DataName;
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
    private Long createEmp;
    private LocalDateTime createTime;
    private Long updateEmp;
    private LocalDateTime updateTime;
    private Integer version;

}
