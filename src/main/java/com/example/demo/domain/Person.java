package com.example.demo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 测试注入实体
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@ApiModel(value = "Person", description = "测试注入实体")
@Data
@Component
@ConfigurationProperties(prefix = "person")
@PropertySource(value = {"classpath:props/my.properties", "file:${person.home}/my.properties"}, ignoreResourceNotFound = true)
public class Person implements Serializable {

    private static final long serialVersionUID = 5342552067095078712L;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", position = 1)
    private String name;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄", position = 2)
    private String age;

//    @Value("${test.props.inner}")
//    private String inner;
//    @Value("${test.props.outer}")
//    private String outer;

}
