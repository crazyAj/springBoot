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
@ApiModel(value = "User", description = "用户")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel implements Serializable {

    private static final long serialVersionUID = -7590840938198975720L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", position = 1)
    private String userName;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", position = 2)
    private String realName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", position = 2)
    private String password;

    /**
     * 加解密salt
     */
    @ApiModelProperty(value = "加解密salt", position = 2)
    private String salt;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", position = 3)
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", position = 4)
    private String email;

    /**
     * 性别：0 保密，1 男，2 女
     */
    @ApiModelProperty(value = "性别：0 保密，1 男，2 女", position = 5)
    private Integer sex = 0;

}