package com.example.demo.utils.ds;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * mybatisplus 配置、多数据源配置
 */
@Configuration
@MapperScan(basePackages = {"com.example.demo.dao", "com.baomidou.mybatisplus.samples.quickstart.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisPlusConfiguration {

    @Resource
    private MybatisPlusProperties mybatisPlusProperties;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.master")
    public AtomikosDataSourceBean masterDataSource() {
        return new AtomikosDataSourceBean();
    }

//    /**
//     * 手动注入，创建 XA 数据源
//     * @return
//     * @throws PropertyException
//     */
//    @Primary
//    @Bean
//    public AtomikosDataSourceBean masterDataSource() throws PropertyException {
//        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
//        Map<String, Object> map = PropertyUtils.getProperties(dataSourceMasterProperties);
//        dataSource.setUniqueResourceName(map.get("uniqueResourceName") == null ? null : map.get("uniqueResourceName").toString());
//        dataSource.setXaDataSourceClassName(map.get("xaDataSourceClassName") == null ? null : map.get("xaDataSourceClassName").toString());
//
//        Properties properties = new Properties();
//        properties.putAll(map);
//        dataSource.setXaProperties(properties);
//        return dataSource;
//    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.atomikos.slave")
    public AtomikosDataSourceBean slaveDataSource() {
        return new AtomikosDataSourceBean();
    }

    @Bean(name = "sqlSessionTemplate")
    public CustomSqlSessionTemplate customSqlSessionTemplate() throws Exception {
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>() {{
            put(DataSourceEnum.MASTER.getValue(), createSqlSessionFactory(masterDataSource()));
            put(DataSourceEnum.SLAVE.getValue(), createSqlSessionFactory(slaveDataSource()));
        }};

        CustomSqlSessionTemplate customSqlSessionTemplate = new CustomSqlSessionTemplate(sqlSessionFactoryMap.get(DataSourceEnum.MASTER.getValue()));
        customSqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryMap);
        return customSqlSessionTemplate;
    }

    private SqlSessionFactory createSqlSessionFactory(AtomikosDataSourceBean dataSource) throws Exception {
        // 项目启动则初始化连接，设置数据源
        dataSource.init();

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        // 解决 jar包启动时MyBatis无法定位实体类
        sqlSessionFactory.setVfs(SpringBootVFS.class);

        // 别名
        if (StringUtils.isNotEmpty(this.mybatisPlusProperties.getTypeAliasesPackage())) {
            sqlSessionFactory.setTypeAliasesPackage(this.mybatisPlusProperties.getTypeAliasesPackage());
        }

        // 类型转换
        if (StringUtils.isNotEmpty(this.mybatisPlusProperties.getTypeHandlersPackage())) {
            sqlSessionFactory.setTypeHandlersPackage(this.mybatisPlusProperties.getTypeHandlersPackage());
        }

        // xml 位置
        if (!ObjectUtils.isEmpty(this.mybatisPlusProperties.resolveMapperLocations())) {
            sqlSessionFactory.setMapperLocations(this.mybatisPlusProperties.resolveMapperLocations());
        }

        // 设置 mybatis 配置
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(this.mybatisPlusProperties.getConfiguration().isMapUnderscoreToCamelCase());
        configuration.setCacheEnabled(this.mybatisPlusProperties.getConfiguration().isCacheEnabled());
        configuration.setJdbcTypeForNull(this.mybatisPlusProperties.getConfiguration().getJdbcTypeForNull());
        sqlSessionFactory.setConfiguration(configuration);

        // 重写 GlobalConfig，注入到 sqlSessionFactory 使其生效
        CustomGlobalConfig globalConfig = new CustomGlobalConfig();
        globalConfig.setBanner(this.mybatisPlusProperties.getGlobalConfig().isBanner());
        globalConfig.setDbConfig(this.mybatisPlusProperties.getGlobalConfig().getDbConfig());
        sqlSessionFactory.setGlobalConfig(globalConfig);

        // 分页插件
        sqlSessionFactory.setPlugins(paginationInterceptor());

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

/**
 * mybatis-plus 自动填充，需要注解
 *
 * @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
 */
@Component
class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("lastUpdateTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
    }

}