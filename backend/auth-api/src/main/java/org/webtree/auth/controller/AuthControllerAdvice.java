package org.webtree.auth.controller;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.exception.AuthenticationException;
import org.webtree.auth.service.AuthenticationService;
import org.webtree.auth.service.JwtTokenService;

@ControllerAdvice(annotations = RestController.class)
public class AuthControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> badUserNameHandler(AuthenticationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ JwtTokenService.InvalidTokenException.class, ExpiredJwtException.class })
    public ResponseEntity<?> unauthorizedHandler(Exception e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("{\"message\":\"" + e.getMessage() + "\"}");
    }

    @ExceptionHandler(AuthenticationService.UserAlreadyRegistered.class)
    public ResponseEntity<?> userAlreadyRegistered(AuthenticationService.UserAlreadyRegistered e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
