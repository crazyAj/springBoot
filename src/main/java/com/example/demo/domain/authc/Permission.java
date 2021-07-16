package com.example.demo.domain.authc;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "Permission", description = "权限")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseModel implements Serializable {

    private static final long serialVersionUID = 264365427465669818L;

    /**
     * 权限名
     */
    @ApiModelProperty(value = "权限名", position = 1)
    private String permissionName;

    @Builder(toBuilder = true)
    public Permission(String id, LocalDateTime createTime, String createBy, LocalDateTime lastUpdateTime, String updateBy, Integer deleteFlag, String remark, String dataSource, String permissionName) {
        super(id, createTime, createBy, lastUpdateTime, updateBy, deleteFlag, remark, dataSource);
        this.permissionName = permissionName;
    }

}