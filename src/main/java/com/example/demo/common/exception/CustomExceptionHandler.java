package com.example.demo.common.exception;

import com.example.demo.common.AppConstant;
import com.example.demo.domain.base.BaseResult;
import com.example.demo.utils.fileUtils.FileFunc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获
 * 继承 ErrorController + @ControllerAdvice + @ExceptionHandle 处理一切异常
 *
 * @ControllerAdvice + @ExceptionHandle 可以处理除 404 以外的运行异常
 * ErrorController 处理捕获不到的异常就是404了
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public BaseResult exceptionHandle(Exception ex) {
        String code;
        String msg;

        if (ex instanceof CustomException) {
            // 业务返回异常，直接返回前端，不做记录 BIXXXX
            CustomException e = ((CustomException) ex);
            code = e.getCode();
            msg = e.getMsg();

            // 如果没有相应的业务类型 SERVICE_UNAVAILABLE
            if (!FileFunc.getProp(AppConstant.EXCEPTION_CODE_PATH).containsKey(code)) {
                code = String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value());
                msg = HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
            }

        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            // 方法类型异常 HttpStatus.METHOD_NOT_ALLOWED = 405
            code = String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value());
            msg = HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase();

        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            // 不支持参数类型异常 HttpStatus.UNSUPPORTED_MEDIA_TYPE = 415
            code = String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
            msg = HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase();

        } else if (ex instanceof TypeMismatchException
                || ex instanceof HttpMessageNotReadableException
                || ex instanceof MissingServletRequestParameterException) {

            // 参数异常 HttpStatus.BAD_REQUEST = 400
            code = String.valueOf(HttpStatus.BAD_REQUEST.value());
            msg = HttpStatus.BAD_REQUEST.getReasonPhrase();

        } else {
            // 服务器异常 HttpStatus.INTERNAL_SERVER_ERROR = 500
            code = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
            msg = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            log.error("Exception errorCode:{}, errorMessage:{}, exception:{}", code, msg, ex);
        }

        return BaseResult.builder().code(code).message(msg).build();
    }

}

@Controller
class NotFoundException implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public BaseResult notFount() {
        String code = String.valueOf(HttpStatus.NOT_FOUND.value());
        String msg = HttpStatus.NOT_FOUND.getReasonPhrase();
        return BaseResult.builder().code(code).message(msg).build();
    }

}