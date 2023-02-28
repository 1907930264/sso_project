package com.yxj.ssoserver.controller;

import com.yxj.ssoserver.common.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionController {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionController.class);


    @ExceptionHandler({Exception.class})
    public RestResponse exception(Exception e){
        log.error(e.getMessage(),e);
        return RestResponse.fail(e.getMessage());
    }
}
