package com.example.demo.domain.authc;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "User", description = "用户")
@NoArgsConstructor
@Getter
@Setter
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

    /**
     * 解决子类继承没有办法 @Builder 父类问题
     */
    @Builder(toBuilder = true)
    public User(String id, LocalDateTime createTime, String createBy, LocalDateTime lastUpdateTime, String updateBy, Integer deleteFlag, String remark, String dataSource, String userName, String realName, String password, String salt, String phone, String email, Integer sex) {
        super(id, createTime, createBy, lastUpdateTime, updateBy, deleteFlag, remark, dataSource);
        this.userName = userName;
        this.realName = realName;
        this.password = password;
        this.salt = salt;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
    }

}