package com.example.demo.domain.authc;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色权限关联表
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "RolePermission", description = "角色权限关联表")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RolePermission extends BaseModel implements Serializable {
    private static final long serialVersionUID = 4970103233080138824L;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", position = 1)
    private String roleId;
    /**
     * 权限id
     */
    @ApiModelProperty(value = "权限id", position = 2)
    private String permissionId;

    @Builder(toBuilder = true)
    public RolePermission(String id, LocalDateTime createTime, String createBy, LocalDateTime lastUpdateTime, String updateBy, Integer deleteFlag, String remark, String dataSource, String roleId, String permissionId) {
        super(id, createTime, createBy, lastUpdateTime, updateBy, deleteFlag, remark, dataSource);
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

}