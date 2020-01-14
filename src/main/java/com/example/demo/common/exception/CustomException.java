package com.example.demo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -7017411287972434840L;

    private String code;
    private String msg;

}

@Getter
enum CustomExceptionEnum {
    /* ---------------- 系统异常状态码 ----------------*/
    SYSTEM_ERROR("EX0000", "服务开小差了，请稍后再试"),
    ILLEGAL_ARGUMENT_EX("EX0001", "非法参数"),
    HTTPCLIENT_ERROR_EX("EX0002", "客户端的请求异常"),

    /* ---------------- 业务状态码 ----------------*/
    SUCCESS("BI0000", "调用成功"),
    NO_BUSINESS_EX("BI9999", "业务异常");

    private String code;
    private String msg;

    CustomExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CustomExceptionEnum getByCode(String code) {
        List<CustomExceptionEnum> res = Arrays
                .stream(CustomExceptionEnum.values())
                .filter(t -> t.code.equals(code))
                .collect(Collectors.toList());

        return CollectionUtils.isEmpty(res) ? null : res.get(0);
    }
}
