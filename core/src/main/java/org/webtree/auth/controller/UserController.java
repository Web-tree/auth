package org.webtree.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.service.UserAuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping
public class UserController extends AbstractController {

    private UserAuthenticationService service;

    @Autowired
    public UserController(UserAuthenticationService service) {
        this.service = service;
    }

    @PostMapping("#{AuthPropertiesBean.route.register}")
    @ResponseStatus(HttpStatus.CREATED)
    public WtUserDetails register(@RequestBody @Valid AuthDetailsImpl authDetails) {
        return service.register(authDetails);
    }
}