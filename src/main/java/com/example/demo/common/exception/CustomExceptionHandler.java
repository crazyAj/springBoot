package com.example.demo.common.exception;

import com.example.demo.common.AppConstant;
import com.example.demo.domain.base.BaseResult;
import com.example.demo.utils.fileUtils.FileFunc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

/**
 * 全局异常捕获
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public BaseResult exceptionHandle(Exception exception) {
        String code = "";
        String msg = "";

        // 业务返回异常，直接返回前端，不做记录
        if (exception instanceof CustomException) {
            CustomException ex = ((CustomException) exception);
            code = ex.getCode();
            msg = ex.getMsg();

            // 如果没有相应的业务类型
            if (!FileFunc.getProp(AppConstant.EXCEPTION_CODE_PATH).contains(code)) {
                code = CustomExceptionEnum.NO_BUSINESS_EX.getCode();
                msg = CustomExceptionEnum.NO_BUSINESS_EX.getMsg();
            }

        } else if (exception instanceof IllegalArgumentException) {
            // 非法入参
            code = CustomExceptionEnum.ILLEGAL_ARGUMENT_EX.getCode();
            msg = CustomExceptionEnum.ILLEGAL_ARGUMENT_EX.getMsg();
            log.error("IllegalArgumentException errorCode:{}, errorMessage:{}, exception:{}", code, msg, exception);

        } else if (exception instanceof HttpClientErrorException) {
            // 针对客户端的请求异常，直接抛出异常的返回结果，不进行处理
            code = CustomExceptionEnum.HTTPCLIENT_ERROR_EX.getCode();
            msg = CustomExceptionEnum.HTTPCLIENT_ERROR_EX.getMsg();
            log.error("HttpClientErrorException errorCode:{}, errorMessage:{}, exception:{}", code, msg, exception);

        } else {
            // 其它异常 SYSTEM_ERROR
            code = CustomExceptionEnum.SYSTEM_ERROR.getCode();
            msg = CustomExceptionEnum.SYSTEM_ERROR.getMsg();
            log.error("Exception errorCode:{}, errorMessage:{}, exception:{}", code, msg, exception);
        }

        return BaseResult.builder()
                .code(code)
                .message(msg)
                .build();
    }

}
