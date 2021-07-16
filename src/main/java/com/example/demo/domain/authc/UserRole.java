package com.example.demo.domain.authc;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户角色关联表
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "UserRole", description = "用户角色关联表")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseModel implements Serializable {
    private static final long serialVersionUID = 5895071740344126204L;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", position = 1)
    private String userId;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", position = 2)
    private String roleId;

    @Builder(toBuilder = true)
    public UserRole(String id, LocalDateTime createTime, String createBy, LocalDateTime lastUpdateTime, String updateBy, Integer deleteFlag, String remark, String dataSource, String userId, String roleId) {
        super(id, createTime, createBy, lastUpdateTime, updateBy, deleteFlag, remark, dataSource);
        this.userId = userId;
        this.roleId = roleId;
    }

}