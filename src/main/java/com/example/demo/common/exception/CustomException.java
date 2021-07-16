package com.example.demo.common.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 通用异常类
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CustomException", description = "通用异常类")
public class CustomException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -7017411287972434840L;

    /**
     * 异常码
     */
    @ApiModelProperty(value = "异常码")
    private String code;
    /**
     * 异常信息
     */
    @ApiModelProperty(value = "异常信息")
    private String msg;

}

