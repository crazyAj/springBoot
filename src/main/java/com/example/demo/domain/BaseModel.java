package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 6964872568460956216L;

    @TableId(type = IdType.UUID)
    private String unid;

    /* 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /* 创建人 */
    private String createBy;

    /* 最后修改时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdateTime;

    /* 更新人 */
    private String updateBy;

    /* 删除标志 */
    @TableLogic
    private Integer deleteFlag;

    /* 备注 */
    private String remark;

}
