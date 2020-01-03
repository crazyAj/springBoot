package com.example.demo.utils.ds;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import lombok.ToString;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 重写 mybaitsplus自带的 GlobalConfig的getSqlSessionFactory 方法
 */
@ToString
@Component
public class CustomGlobalConfig extends GlobalConfig {

    @Autowired
    private CustomSqlSessionTemplate sqlSessionTemplate;

    private static CustomSqlSessionTemplate customSqlSessionTemplate;

    @PostConstruct
    public void init() {
        customSqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return customSqlSessionTemplate.getSqlSessionFactory();
    }

}
