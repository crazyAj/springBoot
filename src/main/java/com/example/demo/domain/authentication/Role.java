package com.example.demo.domain.authentication;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

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
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseModel implements Serializable {
    private static final long serialVersionUID = -550968678846047757L;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", position = 1)
    private String roleName;
}