package com.example.demo.domain.authc;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "Role", description = "角色")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseModel implements Serializable {
    private static final long serialVersionUID = -550968678846047757L;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", position = 1)
    private String roleName;

    @Builder(toBuilder = true)
    public Role(String id, LocalDateTime createTime, String createBy, LocalDateTime lastUpdateTime, String updateBy, Integer deleteFlag, String remark, String dataSource, String roleName) {
        super(id, createTime, createBy, lastUpdateTime, updateBy, deleteFlag, remark, dataSource);
        this.roleName = roleName;
    }

}