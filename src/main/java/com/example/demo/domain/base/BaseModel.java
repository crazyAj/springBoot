package com.example.demo.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.example.demo.common.dataSource.DataSourceContextHolder;
import com.example.demo.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel(value = "BaseModel", description = "通用模型")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 6964872568460956216L;

    // position  控制 doc 排序
    @ApiModelProperty(value = "唯一编号", position = -8)
    @TableId
    public String unid;

    /*
     * 创建时间
     * @JsonFormat  序列化（bean转json）时的格式
     * @DateTimeFormat 反序列（json转bean）时的格式
     */
    @ApiModelProperty(value = "创建时间", position = -7)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    /* 创建人 */
    @ApiModelProperty(value = "创建人", position = -6)
    public String createBy;

    /*
     * 最后修改时间
     * @JsonFormat  序列化（bean转json）时的格式
     * @DateTimeFormat 反序列（json转bean）时的格式
     */
    @ApiModelProperty(value = "最后修改时间", position = -5)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime lastUpdateTime;

    /* 更新人 */
    @ApiModelProperty(value = "更新人", position = -4)
    public String updateBy;

    /* 删除标志 */
    @ApiModelProperty(value = "删除标志", position = -3)
    @TableLogic
    public Integer deleteFlag;

    /* 备注 */
    @ApiModelProperty(value = "备注", position = -2)
    public String remark;

    /* 用于监控数据源来源 */
    @ApiModelProperty(value = "用于监控数据源来源", position = -1)
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


