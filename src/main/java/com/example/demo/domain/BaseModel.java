package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.example.demo.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 6964872568460956216L;

    @TableId
    public String unid;

    /* 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    /* 创建人 */
    public String createBy;

    /* 最后修改时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime lastUpdateTime;

    /* 更新人 */
    public String updateBy;

    /* 删除标志 */
    @TableLogic
    public Integer deleteFlag;

    /* 备注 */
    public String remark;

    /**
     * 深拷贝，需要实现序列化接口
     *
     * @param obj
     * @param <T>
     * @return
     */
    public <T extends Serializable> T deepClone(T obj) {
        return ObjectUtils.clone(obj);
    }
}


