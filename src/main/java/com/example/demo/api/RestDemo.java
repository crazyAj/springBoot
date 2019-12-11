package com.example.demo.api;

import com.example.demo.domain.Person;
import com.example.demo.extra.rabbitmq.RabbitmqProducer;
import com.example.demo.service.ExampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/rest")
public class RestDemo {

    @Value("${first.name}")
    private String name;
    @Autowired
    private ExampleService exampleService;
    @Autowired
    private RabbitmqProducer rabbitmqProducer;
    @Autowired
    private Person person;

    /**
     * test 配置放jar同目录
     */
    @RequestMapping("/testOuterProps")
    @ResponseBody
    public String testOuterProps() {
        log.info("------ testOuterProps ------");
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>> inner = " + person.getInner());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>> outer = " + person.getOuter());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> name = " + person.getName());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> age = " + person.getAge());
        return person.getName();
    }

    /**
     * test Rabbitmq
     */
    @RequestMapping("/testRabbitmq")
    @ResponseBody
    public String testRabbitmq(HttpServletRequest request,
                               @Value("${rabbitmq.exchange.uniteExchange}") String exchage,
                               @Value("${rabbitmq.queue.uniteKey}") String routingKey) {
        String msg = request.getParameter("msg");
        log.info("--- RestDemo --- testRabbitmq ------- exchage = " + exchage + " --- routingKey = " + routingKey + " --- msg = " + msg);
        rabbitmqProducer.sendRabbitmqMessage(exchage, routingKey, msg);
        return "SUCCESS";
    }

    /**
     * test redirect
     */
    @RequestMapping("/testRedirect")
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
    @RequestMapping("/testDispatcher")
    public String testDispatcher(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getContextPath() + "/WEB-INF/jsp/test.jsp";
        log.info("----- path ----- " + path);
        request.setAttribute("name", name);
        return "test";
    }

    /**
     * test json
     */
    @RequestMapping(value = "/testJson", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String testJson() {
        return "{\"json\":\"test\"}";
    }

    @RequestMapping("/testJsp")
    public String testJsp() {
        log.info("----- testJsp -----");
        return "index";
    }


    /**
     * test cookie
     */
    @RequestMapping("/addCookie")
    public String addCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("spring-boot", "test1234");
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
        return "Add cookie success  &  k_v = " + "spring-boot : test1234";
    }

    @RequestMapping("/getCookie")
    public String getCookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            System.out.println(cookie.getPath() + " - " + cookie.getName() + " - " + cookie.getValue());
        }
        return "Get cookie success !";
    }

}
