package com.example.demo.utils.ds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * mybatisplus 配置、多数据源配置
 */
@Configuration
@MapperScan(basePackages = {"com.example.demo.*.dao", "com.baomidou.mybatisplus.core.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisPlusConfiguration {


    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.master")
    public AtomikosDataSourceBean masterDataSource() {
        return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
    }

}
