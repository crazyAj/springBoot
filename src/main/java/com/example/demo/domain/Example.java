package com.example.demo.domain;

import com.example.demo.domain.base.BaseModel;
import lombok.*;

import java.io.Serializable;

/**
 * @Builder 开启建造者模式
 * @Data = @ToString + @EqualsAndHashCode + @Getter + @Setter
 * 继承弗雷参数，需 callSuper = true
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Example extends BaseModel implements Serializable {
    private static final long serialVersionUID = 5862677660031485811L;
    private String exKey;
    private String exVal;
}
