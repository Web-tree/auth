package org.webtree.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.User;
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
    public ResponseEntity<?> register(@RequestBody AuthDetails authDetails) {
        User user = service.register(authDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("#{AuthPropertiesBean.route.login}")
    public Token login(@RequestBody AuthDetails authDetails) {
        return service.login(authDetails);
    }

    @GetMapping("#{AuthPropertiesBean.route.checkToken}")
    public ResponseEntity<User> checkToken(@RequestBody String token) {
        return ResponseEntity.ok(service.decodeToken(token));
    }
}