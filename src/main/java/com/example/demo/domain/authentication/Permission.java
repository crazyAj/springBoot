package com.example.demo.domain.authentication;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 用户
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "Permission", description = "用户")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseModel implements Serializable {

    private static final long serialVersionUID = 264365427465669818L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", position = 1)
    private String userName;

}