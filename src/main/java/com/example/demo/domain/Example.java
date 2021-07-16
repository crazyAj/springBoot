package com.example.demo.domain;

import com.example.demo.domain.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @Builder 开启建造者模式
 * @Data = @ToString + @EqualsAndHashCode + @Getter + @Setter
 * 继承弗雷参数，需 callSuper = true
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "Example", description = "示例实体")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Example extends BaseModel implements Serializable {

    private static final long serialVersionUID = 5862677660031485811L;

    /**
     * key
     */
    @ApiModelProperty(value = "key", position = 1)
    private String exKey;

    /**
     * value
     */
    @ApiModelProperty(value = "value", position = 2)
    private String exVal;

}
