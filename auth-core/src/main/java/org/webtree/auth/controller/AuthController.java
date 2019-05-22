package org.webtree.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.service.AuthenticationService;

@RestController
@RequestMapping
@CrossOrigin(value = "#{AuthPropertiesBean.frontendOrigin}")
public class AuthController {

    private AuthenticationService service;

    @Autowired
    public AuthController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("#{AuthPropertiesBean.route.register}")
    public ResponseEntity<?> register(AuthDetails authDetails) {
        return service.registerIfNotExists(authDetails) ?
                ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("#{AuthPropertiesBean.route.login}")
    public Token login(AuthDetails authDetails) {
        return service.login(authDetails);
    }

    @GetMapping("#{AuthPropertiesBean.route.checkToken}")
    public ResponseEntity<?> checkToken(@RequestBody String token) {
        String username = service.checkToken(token);

        return username != null ?
                ResponseEntity.ok().body(username) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
}