package com.example.demo.api;

import com.example.demo.domain.authc.User;
import com.example.demo.domain.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接口测试类
 *
 * @Author crazyAJ
 * @Date 2021/7/13
 */
@Api(value = "RestDemo", tags = {"demo"})
@Slf4j
@Controller
@RequestMapping("/authc")
public class AuthcDemo {

    /**
     * 测试 shiro 认证
     */
    @ApiOperation(value = "测试 shiro 认证")
    @PostMapping("/login")
    @ResponseBody
    public BaseResult login(@RequestBody User user) {
        String userName = user.getUserName();
        String password = user.getPassword();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return BaseResult.builder()
                    .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                    .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .build();
        }

        // 登录
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);

        return BaseResult.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }

}
