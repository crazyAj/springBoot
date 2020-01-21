package com.example.demo.domain.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回结果
 */
@ApiModel(value = "BaseResult", description = "通用返回接口")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResult<T> implements Serializable {
    private static final long serialVersionUID = 36542704684046792L;

    /* 状态码 */
    @ApiModelProperty(value = "状态码", position = 1)
    public String code;

    /* 状态信息 */
    @ApiModelProperty(value = "状态信息",position = 2)
    public String message;

    /* 附加数据 */
    @ApiModelProperty(value = "附加数据",position = 3)
    public T data;

    public BaseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
