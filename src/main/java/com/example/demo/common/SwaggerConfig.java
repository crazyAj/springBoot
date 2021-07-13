package com.example.demo.common;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 配置
 * swagger-bootstrap-ui 访问路径    http://ip:port/context-path/doc.html
 * springfox-swagger-ui 访问路径    http://ip:port/context-path/swagger-ui.html
 * 注解使用：
 * @Api(value = "RestDemo", description = "测试接口Demo", tags = {"demo"})
 * Controller注解
 * @ApiOperation(value = "测试分布式事务")
 * 方法注解
 * @ApiModel(value = "BaseResult", description = "通用返回接口")
 * 实体类注解。当配置完毕后：
 * 如果有 @RequestBody 注解的，会自动生成入参文档；
 * 如果有 @ResponseBody 注解的，会自动根据返回值类型生成出参文档。
 * @ApiModelProperty(value = "状态码", position = 1)
 * 实体类属性注解，position决定顺序，默认为 0
 * @ApiImplicitParams({
 *     @ApiImplicitParam(name = "msg", value = "消息", required = true)
 * })
 * 可以指定普通入参注释
 * @ApiOperationSupport(
 *     // json 层级，忽略格式：ignoreParameters={"uptModel.id","uptModel.uptPo.id"}
 *     ignoreParameters = {"exchange", "routingKey"}
 * )
 * 忽略入参类型
 * @DynamicParameters(name = "CreateOrderMapModel",properties = {
 *         @DynamicParameter(name = "id",value = "注解id",example = "X000111",required = true,dataTypeClass = Integer.class),
 *         @DynamicParameter(name = "name",value = "订单编号"),
 *         @DynamicParameter(name = "orderInfo",value = "订单信息",dataTypeClass = Order.class),
 * })
 * Map，JsonObject 类型入参
 * @ApiOperationSupport(
 *     responses = @DynamicResponseParameters(properties = {
 *         @DynamicParameter(value = "编号",name = "id"),
 *         @DynamicParameter(value = "名称",name = "name"),
 *         @DynamicParameter(value = "订单",name = "orderDate",dataTypeClass = OrderDate.class)
 *     })
 * )
 * Map，JsonObject 类型出参
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Value("${my.app.name}")
    private String name;
    @Value("${my.app.version}")
    private String version;
    @Value("${my.app.api.basePackage}")
    private String basePackage;
    @Value("${my.app.contact.name}")
    private String contactName;
    @Value("${my.app.contact.url}")
    private String contactUrl;
    @Value("${my.app.contact.email}")
    private String contactEmail;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(name)
                        .description(name + " 接口管理文档")
                        .version(version)
                        .contact(new Contact(contactName,
                                contactUrl,
                                contactEmail))
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

}
