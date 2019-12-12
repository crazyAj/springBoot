package com.example.demo.utils.ds;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重写 mybaitsplus自带的 GlobalConfig的getSqlSessionFactory 方法
 */
@Component
public class CustomGlobalConfig extends GlobalConfig {

    @Autowired
    private CustomSqlSessionTemplate sqlSessionTemplate;

//    private static CustomSqlSessionTemplate mySqlSessionTemplate;
//
//    @PostConstruct
//    public void init() {
//        this.mySqlSessionTemplate = sqlSessionTemplate;
//    }

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionTemplate.getSqlSessionFactory();
    }

}