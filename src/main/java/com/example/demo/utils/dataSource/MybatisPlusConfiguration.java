package com.example.demo.utils.dataSource;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
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

    /*
     *  配置文件中不能再进行配置，否则手动注入的实体会不生效
     *
     *  # 映射文件所在路径
     *  mybatis-plus.mapper-locations=classpath*:xml/mapper/*.xml
     *  # pojo类所在包路径
     *  mybatis-plus.type-aliases-package=com.example.demo.domain
     *  # 自动转换小驼峰 （configuration 需要在 MybatisPlusConfiguration 对应配置）
     *  mybatis-plus.configuration.map-underscore-to-camel-case=true
     *  # 全局地开启或关闭配置文件中的所有映射器已经配置的任何缓存
     *  mybatis-plus.configuration.cache-enabled=false
     *  # 注意：单引号
     *  mybatis-plus.configuration.jdbc-type-for-null='null'
     *  # 关闭 logo
     *  mybatis-plus.global-config.banner=false
     *  # 主键类型  0:"数据库ID自增", 1:"未设置主键类型",2:"用户输入ID (该类型可以通过自己注册自动填充插件进行填充)", 3:"全局唯一ID (idWorker), 4:全局唯一ID (UUID), 5:字符串全局唯一ID (idWorker 的字符串表示)";
     *  mybatis-plus.global-config.db-config.id-type=UUID
     *  # 字段策略 IGNORED:"忽略判断", NOT_NULL:"非 NULL 判断", NOT_EMPTY:"非空判断"
     *  mybatis-plus.global-config.db-config.insert-strategy=not_empty
     *  mybatis-plus.global-config.db-config.update-strategy=not_empty
     *  mybatis-plus.global-config.db-config.select-strategy=not_empty
     *  # 逻辑前的值 默认值0
     *  mybatis-plus.global-config.db-config.logic-not-delete-value=0
     *  # 删除后的状态 默认值1
     *  mybatis-plus.global-config.db-config.logic-delete-value=1
     */
    private SqlSessionFactory createSqlSessionFactory(AtomikosDataSourceBean dataSource) throws Exception {
        // 项目启动则初始化连接，设置数据源
        dataSource.init();

        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        // 解决 jar包启动时MyBatis无法定位实体类
        sqlSessionFactory.setVfs(SpringBootVFS.class);

        // 别名
        sqlSessionFactory.setTypeAliasesPackage("com.example.demo.domain");

        // xml 位置
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/xml/mapper/*.xml"));

        // 设置 mybatis 配置
        MybatisConfiguration configuration = new MybatisConfiguration();
        // 自动转换小驼峰 （configuration 需要在 MybatisPlusConfiguration 对应配置）
        configuration.setMapUnderscoreToCamelCase(true);
        // 全局地开启或关闭配置文件中的所有映射器已经配置的任何缓存
        configuration.setCacheEnabled(false);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);

        GlobalConfig.DbConfig dbConfig = GlobalConfigUtils.defaults().getDbConfig();
        /* 主键类型:
         *  0: 数据库ID自增
         *  1: 未设置主键类型
         *  2: 用户输入ID (该类型可以通过自己注册自动填充插件进行填充)
         *  3: 全局唯一ID (idWorker)
         *  4: 全局唯一ID (UUID)
         *  5:字符串全局唯一ID (idWorker 的字符串表示)
         */
        dbConfig.setIdType(IdType.ASSIGN_UUID);
        // 字段策略 IGNORED:"忽略判断", NOT_NULL:"非 NULL 判断", NOT_EMPTY:"非空判断"
        dbConfig.setInsertStrategy(FieldStrategy.NOT_EMPTY);
        // 字段策略 IGNORED:"忽略判断", NOT_NULL:"非 NULL 判断", NOT_EMPTY:"非空判断"
        dbConfig.setUpdateStrategy(FieldStrategy.NOT_EMPTY);
        // 字段策略 IGNORED:"忽略判断", NOT_NULL:"非 NULL 判断", NOT_EMPTY:"非空判断"
        dbConfig.setSelectStrategy(FieldStrategy.NOT_EMPTY);
        // 逻辑前的值 默认值0
        dbConfig.setLogicNotDeleteValue("0");
        // 删除后的状态 默认值1
        dbConfig.setLogicDeleteValue("1");

        // 重写 GlobalConfig，注入到 sqlSessionFactory 使其生效
        CustomGlobalConfig globalConfig = new CustomGlobalConfig();
        // 关闭 logo
        globalConfig.setBanner(false);
        globalConfig.setDbConfig(dbConfig);
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