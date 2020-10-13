package com.example.demo.api;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Example;
import com.example.demo.domain.Person;
import com.example.demo.domain.base.BaseResult;
import com.example.demo.service.ExampleService;
import com.example.demo.utils.file.fileUtils.FileFunc;
import com.example.demo.utils.file.word.WordDonload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api(value = "RestDemo", tags = {"demo"})
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

    /**
     * 测试获取 GET
     *
     * @param request
     * @return
     */
    @ApiIgnore
    @GetMapping("/testGet")
    @ResponseBody
    public String testGet(HttpServletRequest request) {
        System.out.println(String.format("--- remote ip --- %s", request.getRemoteAddr()));

        System.out.println("--- GET --- headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null && headerNames.hasMoreElements()) {
            System.out.println(JSONObject.toJSONString(Collections.list(headerNames).stream()
                    .collect(Collectors.toMap(t -> t, t -> Collections.list(request.getHeaders(t))))));
        }

        System.out.println("--- GET --- params:");
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && parameterMap.size() > 0) {
            System.out.println(JSONObject.toJSONString(parameterMap));
        }
        return "success";
    }

    /**
     * 测试获取 POST
     *
     * @param request
     * @param body
     * @return
     */
    @ApiIgnore
    @PostMapping("/testPost")
    @ResponseBody
    public String testPost(HttpServletRequest request, @RequestBody JSONObject body) throws InterruptedException {
        System.out.println(String.format("--- remote ip --- %s", request.getRemoteAddr()));

        System.out.println("--- POST --- headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null && headerNames.hasMoreElements()) {
            System.out.println(JSONObject.toJSONString(Collections.list(headerNames).stream()
                    .collect(Collectors.toMap(t -> t, t -> Collections.list(request.getHeaders(t))))));
        }

        System.out.println("--- POST --- cookie:");
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(JSONObject.toJSONString(cookie));
        }

        System.out.println("--- POST --- session:");
        HttpSession session = request.getSession();
        Enumeration<String> sessionNames = session.getAttributeNames();
        if (sessionNames != null && sessionNames.hasMoreElements()) {
            System.out.println(JSONObject.toJSONString(Collections.list(sessionNames).stream()
                    .collect(Collectors.toMap(t -> t, t -> session.getAttribute(t)))));
        }

        System.out.println("--- POST --- params:");
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && parameterMap.size() > 0) {
            System.out.println(JSONObject.toJSONString(parameterMap));
        }

        System.out.println("--- POST --- body:");
        System.out.println(body);
        TimeUnit.SECONDS.sleep(3);
        return "success";
    }

    @ApiIgnore
    @GetMapping("/htmlToword")
    @ResponseBody
    public void htmlToword(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("equipmentId", "59-ZX-1003");
            data.put("equipmentName", "卧式加工中心");
            data.put("workDay", "2020-06-18");
            data.put("duration", "15");
            data.put("executer", "梁佳、aj");
            data.put("type", "cnc故障");
            data.put("phenomenon", "现象是一个汉语词语，读音为xiàn xiàng。现象是事物表现出来的，能被人感觉到的一切情况。现象是人能够看到、听到、闻到、触摸到的。按照是否有自然属性来分，现象可分为自然现象和社会现象。");
            data.put("reason", "原因yuán yīn是对事物所以如此的解释。出自《水浒传》第四十四回:“原因押送花石纲,要造大舡,嗔怪这提调官催并责罸他,把本官一时杀了。”");
            data.put("deal", "处理是汉语词汇，读作chǔ lǐ，意思是处置、安排、加工，快速的解决问题，也泛指低价出售。");
            data.put("remark", "无");
            data.put("changes", new ArrayList<ArrayList<String>>() {{
                add(new ArrayList<String>() {{
                    add("59-ZX-1003");
                    add("重庆悟空商务管理有限公司");
                    add("ZX-1003");
                    add("100");
                    add("$2000");
                }});
                add(new ArrayList<String>() {{
                    add("259-ZX-1003");
                    add("2重庆悟空商务管理有限公司");
                    add("2ZX-1003");
                    add("2100");
                    add("2$2000");
                }});
            }});
            String fileName = "设备维修记录" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHhhss"));
            String content = getHtmlTemplate(data);
            WordDonload.exportWord(request, response, fileName, content);
        } catch (Exception e) {
            log.error("html2word ERROR {}", e);
        }
    }

    private String getHtmlTemplate(Map<String, Object> data) {
        String res = "<html>\n" +
                "  <head>\n" +
                "" +
                "    <style>\n" +
                "      div > span {\n" +
                "        font-size: 12pt;\n" +
                "        font-family: SimSun;\n" +
                "      }\n" +
                "      .box {\n" +
                "        font-size: 12pt;\n" +
                "        font-family: SimSun;\n" +
                "      }\n" +
                "      table {\n" +
                "        width: 15cm;\n" +
                "        border: 1px solid;\n" +
                "        border-collapse: collapse;\n" +
                "      }\n" +
                "      td {\n" +
                "        height: 30px;\n" +
                "        border: 1px solid;\n" +
                "        font-size: 12pt;\n" +
                "        font-family: SimSun;\n" +
                "        word-wrap:break-word;\n" +
                "        word-break:break-all;\n" +
                "        vertical-align:middle;\n" +
                "      }\n" +
                "      table td:nth-child(1) {\n" +
                "        width: 1.5cm;\n" +
                "      }\n" +
                "      table td:nth-child(2) {\n" +
                "        width: 3cm;\n" +
                "      }\n" +
                "      table td:nth-child(3) {\n" +
                "        width: 4.5cm;\n" +
                "      }\n" +
                "      table td:nth-child(4) {\n" +
                "        width: 3cm;\n" +
                "      }\n" +
                "      table td:nth-child(5) {\n" +
                "        width: 1.5cm;\n" +
                "      }\n" +
                "      table td:nth-child(6) {\n" +
                "        width: 1.5cm;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div>\n" +
                "      <div style=\"text-align: center; font-size: 16pt; font-family: SimSun; font-weight: bold;\">\n" +
                "        设备维修记录\n" +
                "      </div>\n" +
                "      <br/>\n" +
                "      <table>\n" +
                "        <tr>\n" +
                "            <td style=\"width: 5cm;\">设备编号：</td>\n" +
                "            <td style=\"width: 5cm;\">" + data.get("equipmentId") + "</td>\n" +
                "            <td style=\"width: 5cm;\">设备名称：</td>\n" +
                "            <td style=\"width: 5cm;\">" + data.get("equipmentName") + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"width: 5cm;\">故障日期：</td>\n" +
                "            <td style=\"width: 5cm;\">" + data.get("workDay") + "</td>\n" +
                "            <td style=\"width: 5cm;\">停机时间(s)：</td>\n" +
                "            <td style=\"width: 5cm;\">" + data.get("duration") + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"width: 5cm;\">操 作 者：</td>\n" +
                "            <td style=\"width: 5cm;\">" + data.get("executer") + "</td>\n" +
                "            <td style=\"width: 5cm;\">异常部位：</td>\n" +
                "            <td style=\"width: 5cm;\">" + data.get("type") + "</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "      <br/>\n" +
                "      <div class=\"box\">现象症状：</div>\n" +
                "      <table>\n" +
                "        <tr>\n" +
                "          <td>" + data.get("phenomenon") + "</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "      <br/>\n" +
                "      <div class=\"box\">原因：</div>\n" +
                "      <table>\n" +
                "        <tr>\n" +
                "          <td>" + data.get("reason") + "</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "      <br/>\n" +
                "      <div class=\"box\">处理措施：</div>\n" +
                "      <table>\n" +
                "        <tr>\n" +
                "          <td>" + data.get("deal") + "</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "      <br/>\n" +
                "      <div>\n" +
                "        <span>更换部件：</span>\n" +
                "<table>" +
                "<tr style=\"text-align: center;\">" +
                "<td>NO</td>" +
                "<td>名称</td>" +
                "<td>厂家</td>" +
                "<td>型号</td>" +
                "<td>数量</td>" +
                "<td>单价</td>" +
                "</tr>";

        Object changesObj = data.get("changes");
        if (changesObj == null) {
            res += "<tr>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "<td></td>" +
                    "</tr>";
        } else {
            List<List<String>> changes = (ArrayList) data.get("changes");
            for (int i = 0; i < changes.size(); i++) {
                List<String> list = changes.get(i);
                res += "<tr>" +
                        "<td>" + (i + 1) + "</td>" +
                        "<td>" + (StringUtils.isBlank(list.get(0)) ? "" : list.get(0)) + "</td>" +
                        "<td>" + (StringUtils.isBlank(list.get(1)) ? "" : list.get(1)) + "</td>" +
                        "<td>" + (StringUtils.isBlank(list.get(2)) ? "" : list.get(2)) + "</td>" +
                        "<td>" + (StringUtils.isBlank(list.get(3)) ? "" : list.get(3)) + "</td>" +
                        "<td>" + (StringUtils.isBlank(list.get(4)) ? "" : list.get(4)) + "</td>" +
                        "</tr>";
            }
        }

        res += "        </table>\n" +
                "      </div>\n" +
                "      <br/>\n" +
                "      <div class=\"box\">备注：</div>\n" +
                "      <table>\n" +
                "        <tr>\n" +
                "          <td>" + data.get("remark") + "</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "      <span style=\"font-size: 10.5pt\">检查机械防护完好及所有急停按钮有效□</span>\n" +
                "      <br/><br/><br/><br/>\n" +
                "      <div>\n" +
                "        <span style=\"width: 5cm;\">车间确认：______________</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
                "        <span style=\"width: 5cm;\">设备维修人员签字：______________</span>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
        return res;
    }

}
