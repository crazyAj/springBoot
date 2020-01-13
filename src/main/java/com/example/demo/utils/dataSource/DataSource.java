package com.example.demo.utils.dataSource;

import java.lang.annotation.*;

/**
 * DataSource.java 数据源注解
 * DataSourceEnum.java 数据源枚举类
 * 提供获取枚举的方法
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataSource {
    DataSourceEnum value() default DataSourceEnum.MASTER;
}
