package com.v4ward.operate.log.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NieYinjun
 * @date 2019/1/5 12:24
 * @tag
 */
@Data
public class OperateDataDTO {
    private Object id;

    private List<UpdateDTO> updates;

    public OperateDataDTO(){
        this.updates=new ArrayList<>();
    }

    public void addUpdate(UpdateDTO updateDTO){
        this.updates.add(updateDTO);
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
