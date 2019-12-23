package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.example.demo.Temp;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.Serializable;

@Data
public class Example implements Serializable {

    private static final long serialVersionUID = 5862677660031485811L;

    @TableId
    private String unid;
    private String exKey;
    private String exVal;

}
