package com.example.demo.domain.base;

import com.baomidou.mybatisplus.annotation.*;
import com.example.demo.common.dataSource.DataSourceContextHolder;
import com.example.demo.utils.DateFormatTool;
import com.example.demo.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 6964872568460956216L;

    @TableId
    public String unid;

    /*
     * 创建时间
     * @JsonFormat  序列化（bean转json）时的格式
     * @DateTimeFormat 反序列（json转bean）时的格式
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    /* 创建人 */
    public String createBy;

    /*
     * 最后修改时间
     * @JsonFormat  序列化（bean转json）时的格式
     * @DateTimeFormat 反序列（json转bean）时的格式
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime lastUpdateTime;

    /* 更新人 */
    public String updateBy;

    /* 删除标志 */
    @TableLogic
    public Integer deleteFlag;

    /* 备注 */
    public String remark;

    /* 用于监控数据源来源 */
    @TableField(exist = false)
    public String dataSource = DataSourceContextHolder.getDataSource();

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


