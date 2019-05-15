package org.webtree.auth.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.exception.AuthenticationException;

@ControllerAdvice(annotations = RestController.class)
public class AuthControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> badUserNameHandler(AuthenticationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
