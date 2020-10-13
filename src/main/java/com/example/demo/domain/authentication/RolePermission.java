package com.example.demo.domain.authentication;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 角色权限关联表
 */
@ApiModel(value = "Role", description = "角色权限关联表")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RolePermission extends BaseModel implements Serializable {
    private static final long serialVersionUID = 4970103233080138824L;
    /* 角色id */
    @ApiModelProperty(value = "角色id", position = 1)
    private String roleId;
    /* 权限id */
    @ApiModelProperty(value = "权限id", position = 2)
    private String permissionId;
}