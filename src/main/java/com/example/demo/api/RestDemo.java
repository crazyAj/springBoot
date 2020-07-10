package com.example.demo.api;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.mq.rabbitmq.RabbitmqProducer;
import com.example.demo.domain.Example;
import com.example.demo.domain.Person;
import com.example.demo.domain.base.BaseResult;
import com.example.demo.service.ExampleService;
import com.example.demo.utils.DateFormatTool;
import com.example.demo.utils.fileUtils.FileFunc;
import com.example.demo.utils.fileUtils.FileMonitor;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Api(value = "RestDemo", description = "测试接口Demo", tags = {"demo"})
@Slf4j
@Controller
@RequestMapping("/rest")
public class RestDemo {

    @Value("${rabbitmq.exchange.first.exchange}")
    private String firstExchange;
    @Value("${rabbitmq.exchange.second.exchange}")
    private String secondExchange;
    @Value("${rabbitmq.queue.first.routing-key}")
    private String firstRoutingKey;
    @Value("${rabbitmq.queue.second.routing-key}")
    private String secondRoutingKey;

    @Value("${first.name}")
    private String name;
    @Autowired
    private Person person;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private ExampleService exampleService;
//    @Autowired
//    private RabbitmqProducer rabbitmqProducer;

    /**
     * 测试 RestTemplate 和 RestTemplateBuilder
     */
    @GetMapping("/testRestTemplate")
    @ResponseBody
    public BaseResult<String> testRestTemplate() {
        String url = "https://www.baidu.com";
        // spring-web
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        restTemplate.setRequestFactory(requestFactory);

        //设置HTTP请求头信息，实现编码等
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Accept-Charset", "utf-8");
        // 设置编码
        requestHeaders.set("Content-type", "text/html; charset=utf-8");

        //利用容器实现数据封装，发送
        HttpEntity<String> entity = new HttpEntity<>("test", requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        int statusCode = responseEntity.getStatusCodeValue();
        String entityBody = responseEntity.getBody();

//        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//        int statusCode = responseEntity.getStatusCodeValue();
//        String entityBody = responseEntity.getBody();
        System.out.println(String.format("--> statusCode = %s, entityBody = %s", statusCode, entityBody));

        // spring-boot
        ResponseEntity<String> responseEntity2 = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build()
                .getForEntity(url, String.class);
        int statusCode2 = responseEntity2.getStatusCodeValue();
        String entityBody2 = responseEntity2.getBody();
        System.out.println(String.format("--> statusCode2 = %s, entityBody2 = %s", statusCode2, entityBody2));

        return BaseResult.<String>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .data(entityBody)
                .build();
    }

    /**
     * 数组第一个元素中的 exKey 字段，控制插入1一个元素的数据源；
     * 数组第一个元素中的 slave 字段，控制插入剩下的元素的数据源
     *
     * @param examples
     * @return
     */
    @ApiOperation(value = "测试分布式事务")
    @PostMapping("/testAddEx")
    @ResponseBody
    public BaseResult<List<Example>> testAddEx(@RequestBody List<Example> examples) {
        List<Example> res = exampleService.testTx(examples);
        return BaseResult.<List<Example>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .data(res)
                .build();
    }

    /**
     * test 配置放jar外
     */
    @ApiOperation(value = "测试配置放jar外")
    @GetMapping("/testRefreshOuterProps")
    @ResponseBody
    public BaseResult<Properties> testRefreshOuterProps(@Value("${person.home}") String dir) {
        Properties prop = FileFunc.getProp(dir + "/my.properties");
        log.info("------ testOuterProps ------ {}", prop);
        return BaseResult.<Properties>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .data(prop)
                .build();
    }

    /**
     * test 配置放jar同目录
     */
    @ApiOperation(value = "测试外部属性自动注入")
    @GetMapping("/testOuterProps")
    @ResponseBody
    public BaseResult<String> testOuterProps() {
        log.info("------ testOuterProps ------");
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>> inner = " + person.getInner());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>> outer = " + person.getOuter());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> name = " + person.getName());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> age = " + person.getAge());
        return BaseResult.<String>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .data(person.getName())
                .build();
    }

    /**
     * test Rabbitmq
     */
   /* @ApiOperation(value = "测试rabbitmq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg", value = "消息", required = true),
            @ApiImplicitParam(name = "choose", value = "发送到哪个服务：first第一个；second第二个", required = true)
    })
    @ApiOperationSupport(
            // json 层级，忽略格式：{"a.b", "a.b2.c"}
            ignoreParameters = {"exchange", "routingKey"}
    )
    @GetMapping("/testRabbitmq")
    @ResponseBody
    public BaseResult testRabbitmq(@RequestParam String msg, @RequestParam String choose) {
        String sendTo;
        if (StringUtils.isNotEmpty(choose) && "first".equalsIgnoreCase(choose)) {
            sendTo = "first";
        } else {
            sendTo = "second";
        }

        log.info("--- RestDemo --- testRabbitmq to {} --- exchage = {} --- routingKey = {} --- msg = {}",
                sendTo,
                "first".equals(sendTo) ? firstExchange : secondExchange,
                "first".equals(sendTo) ? firstRoutingKey : secondRoutingKey,
                msg);

        if ("first".equals(sendTo)) rabbitmqProducer.sendRabbitmqMessageToFirst(firstExchange, firstRoutingKey, msg);
        else rabbitmqProducer.sendRabbitmqMessageToSecond(secondExchange, secondRoutingKey, msg);

        return BaseResult.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }*/

    /**
     * test redirect
     */
    @ApiIgnore
    @GetMapping("/testRedirect")
    public String testRedirect(HttpServletRequest request, HttpServletResponse response) {
        try {
            String s = null;
            s.length();
            String path = "/page/test.html";
            log.info("----- path ----- " + path);
            return "redirect:" + path;
        } catch (Exception e) {
            String path = "/page/error-page.html";
            log.info("----- path ----- " + path);
            return "redirect:" + path;
        }
    }

    /**
     * test redirect
     */
    @ApiIgnore
    @GetMapping("/testDispatcher")
    public String testDispatcher(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getContextPath() + "/WEB-INF/jsp/test.jsp";
        log.info("----- path ----- " + path);
        request.setAttribute("name", name);
        return "test";
    }

    /**
     * test json
     */
    @ApiIgnore
    @GetMapping(value = "/testJson", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String testJson() {
        return "{\"json\":\"test\"}";
    }

    /**
     * test jsp
     *
     * @return
     */
    @ApiIgnore
    @GetMapping("/testJsp")
    public String testJsp() {
        log.info("----- testJsp -----");
        return "index";
    }


    /**
     * test cookie
     * 测试cookie
     */
    @ApiIgnore
    @GetMapping("/addCookie")
    @ResponseBody
    public String addCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("spring-boot", "test1234");
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
        return "Add cookie success  &  k_v = spring-boot : test1234";
    }

    /**
     * 测试获取 cookie
     *
     * @param request
     * @return
     */
    @ApiIgnore
    @GetMapping("/getCookie")
    @ResponseBody
    public String getCookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            System.out.println(cookie.getPath() + " - " + cookie.getName() + " - " + cookie.getValue());
        }
        return "Get cookie success !";
    }

}
