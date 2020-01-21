package com.example.demo.api;

import com.example.demo.domain.Example;
import com.example.demo.domain.Person;
import com.example.demo.domain.base.BaseResult;
import com.example.demo.extra.rabbitmq.RabbitmqProducer;
import com.example.demo.service.ExampleService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "RestDemo", description = "测试接口Demo", tags = {"demo"})
@Slf4j
@Controller
@RequestMapping("/rest")
public class RestDemo {

    @Value("${first.name}")
    private String name;
    @Autowired
    private RabbitmqProducer rabbitmqProducer;
    @Autowired
    private Person person;
    @Autowired
    private ExampleService exampleService;

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
    @ApiOperation(value = "测试rabbitmq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg", value = "消息", required = true)
    })
    @ApiOperationSupport(
            // json 层级，忽略格式：{"a.b", "a.b2.c"}
            ignoreParameters = {"exchange", "routingKey"}
    )
    @GetMapping("/testRabbitmq")
    @ResponseBody
    public BaseResult testRabbitmq(@RequestParam String msg,
                                   @Value("${rabbitmq.exchange.uniteExchange}") String exchange,
                                   @Value("${rabbitmq.queue.uniteKey}") String routingKey) {
        log.info("--- RestDemo --- testRabbitmq ------- exchage = " + exchange + " --- routingKey = " + routingKey + " --- msg = " + msg);
        rabbitmqProducer.sendRabbitmqMessage(exchange, routingKey, msg);
        return BaseResult.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }

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
