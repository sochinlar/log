package com.v4ward.operate.log.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author NieYinjun
 * @date 2019/1/5 12:26
 * @tag
 */
@Data
@Accessors(chain = true)
public class UpdateDTO {
    private Object column;
    private Object oldValue;
    private Object newValue;
}
