package org.webtree.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WtUserDetails;
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
    @ResponseStatus(HttpStatus.CREATED)
    public WtUserDetails register(AuthDetails authDetails) {
        return service.register(authDetails);
    }

    @PostMapping("#{AuthPropertiesBean.route.login}")
    public Token login(AuthDetails authDetails) {
        return service.login(authDetails);
    }
}