package org.webtree.auth.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.WTUserDetails;
import org.webtree.auth.service.UserAuthenticationService;

@RestController
@RequestMapping("/rest/user")
public class UserController extends AbstractController {

    private UserAuthenticationService service;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserAuthenticationService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping("register")
    public WTUserDetails register(@RequestBody AuthDetails authDetails) {
        return service.register(authDetails);
    }
}
