package com.example.Lib_Management_System_BE.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handlebussinessexception (Bussinessexception e) {
        return ResponseEntity.
                status(HttpStatus.CONFLICT).
                body(Map.of("message",e.getMessage()));
    }
}
