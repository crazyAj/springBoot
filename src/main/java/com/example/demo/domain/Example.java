package com.example.demo.domain;

import com.example.demo.domain.base.BaseModel;
import lombok.*;

import java.io.Serializable;

/**
 * @Data = @ToString + @EqualsAndHashCode + @Getter + @Setter
 * 继承弗雷参数，需 callSuper = true
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Example extends BaseModel implements Serializable {
    private static final long serialVersionUID = 5862677660031485811L;
    private String exKey;
    private String exVal;
}
