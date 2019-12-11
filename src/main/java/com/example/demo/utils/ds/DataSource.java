package com.example.demo.utils.ds;

import java.lang.annotation.*;

/**
 * DataSource.java 数据源注解
 * DataSourceEnum.java 数据源枚举类
 * 提供获取枚举的方法
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceEnum value();
}
