package org.webtree.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.Token;
import org.webtree.auth.service.UserAuthenticationService;

@RestController
@RequestMapping
public class SecurityController extends AbstractController {

    private UserAuthenticationService service;

    @Autowired
    public SecurityController(
            UserAuthenticationService userService) {
        this.service = userService;
    }

    @PostMapping("#{propertyBean.getNewTokenUrl()}")
    public Token login(@RequestBody AuthDetailsImpl authDetails) {
        return service.login(authDetails);
    }
}