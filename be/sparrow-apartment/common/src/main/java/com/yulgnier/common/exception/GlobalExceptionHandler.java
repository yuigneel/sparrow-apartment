package com.yulgnier.common.exception;

import com.yulgnier.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handle(Exception e){
        e.printStackTrace();
        return Result.fail();
    }
}
