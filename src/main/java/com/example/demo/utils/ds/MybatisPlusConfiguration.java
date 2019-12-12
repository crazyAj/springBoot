package com.example.demo.utils.ds;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * mybatisplus 配置、多数据源配置
 */
@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
@MapperScan(basePackages = {"com.example.demo.*.dao", "com.baomidou.mybatisplus.core.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisPlusConfiguration {

    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.master")
    public AtomikosDataSourceBean masterDataSource() {
        return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.slave")
    public AtomikosDataSourceBean slaveDataSource() {
        return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
    }

    @Bean(name = "sqlSessionTemplate")
    public CustomSqlSessionTemplate customSqlSessionTemplate() throws Exception {
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>() {{
            put(DataSourceEnum.MASTER.getValue(), createSqlSessionFactory(masterDataSource()));
            put(DataSourceEnum.MASTER.getValue(), createSqlSessionFactory(slaveDataSource()));
        }};

        CustomSqlSessionTemplate customSqlSessionTemplate = new CustomSqlSessionTemplate(sqlSessionFactoryMap.get(DataSourceEnum.MASTER.getValue()));
        customSqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryMap);
        return customSqlSessionTemplate;
    }

    private SqlSessionFactory createSqlSessionFactory(AtomikosDataSourceBean dataSource) throws Exception {

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();

        // 项目启动则初始化连接，设置数据源
        dataSource.init();
        sqlSessionFactory.setDataSource(dataSource);

        // 解决 jar包启动时MyBatis无法定位实体类
        sqlSessionFactory.setVfs(SpringBootVFS.class);

        // 读取配置文件路径
        if (StringUtils.isNotEmpty(this.mybatisPlusProperties.getConfigLocation())) {
            sqlSessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(this.mybatisPlusProperties.getConfigLocation()));
        }

        // 设置 mybatis-plus 配置
        sqlSessionFactory.setConfiguration(this.mybatisPlusProperties.getConfiguration());

        // 重写 GlobalConfig，注入到 sqlSessionFactory 使其生效
        CustomGlobalConfig globalConfig = new CustomGlobalConfig();
        globalConfig.setDbConfig(this.mybatisPlusProperties.getGlobalConfig().getDbConfig());
        sqlSessionFactory.setGlobalConfig(globalConfig);

        // 分页插件
        sqlSessionFactory.setPlugins(paginationInterceptor());

        // 别名
        String typeAliasesPackage = this.mybatisPlusProperties.getTypeAliasesPackage();
        if (StringUtils.isNotEmpty(typeAliasesPackage)) {
            sqlSessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        }

        // 类型转换
        if (StringUtils.isNotEmpty(this.mybatisPlusProperties.getTypeHandlersPackage())) {
            sqlSessionFactory.setTypeHandlersPackage(this.mybatisPlusProperties.getTypeHandlersPackage());
        }

        // sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath*:xml/mapper/*.xml"));
        // xml 位置
        if (!ObjectUtils.isEmpty(this.mybatisPlusProperties.resolveMapperLocations())) {
            sqlSessionFactory.setMapperLocations(this.mybatisPlusProperties.resolveMapperLocations());
        }

        // 刷新配置，使之生效
        sqlSessionFactory.afterPropertiesSet();
        return sqlSessionFactory.getObject();
    }

    /*
     * Mybatis-plus 分页插件
     * 自定义的分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
