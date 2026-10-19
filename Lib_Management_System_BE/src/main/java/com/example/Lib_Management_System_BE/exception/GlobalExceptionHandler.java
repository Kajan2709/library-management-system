package com.example.Lib_Management_System_BE.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handlebussinessexception (Bussinessexception e) {
        return ResponseEntity.
                badRequest().
                body(e.getMessage());
    }
}
