package com.example.demo.utils.ds;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 数据源 key 枚举类
 */
public enum DataSourceEnum {

    /* 主数据源 */
    MASTER("master"),
    /* 从数据源 */
    SLAVE("slave");

    @Getter
    private String value;

    DataSourceEnum(String value) {
        this.value = value;
    }

    /**
     * 根据 Method 获取数据源枚举
     *
     * @param method
     * @return
     */
    public static String getByMethod(Method method) {
        DataSourceEnum dataSourceEnum;
        DataSource dataSource = AnnotationUtils.findAnnotation(method, DataSource.class);
        if (dataSource != null) {
            // 注解存在，则以注解为主
            dataSourceEnum = getByAnnotation(dataSource);
        } else {
            // 注解不存在，以方法名区分
            dataSourceEnum = getByMethodName(method.getName());
        }
        return dataSourceEnum.getValue();
    }


    /**
     * 根据注解获取数据源
     *
     * @param dataSource
     * @return
     */
    private static DataSourceEnum getByAnnotation(DataSource dataSource) {
        if (dataSource.value() == DataSourceEnum.SLAVE) {
            return SLAVE;
        }
        return MASTER;
    }

    /**
     * 根据方法名称选择
     *
     * @param name
     * @return
     */
    private static DataSourceEnum getByMethodName(String name) {
        if (StringUtils.isNotEmpty(name) && (name.contains("select")
                || name.contains("query")
                || name.contains("find")
                || name.contains("get")
                || name.contains("list"))) {
            return SLAVE;
        }
        return MASTER;
    }

}
