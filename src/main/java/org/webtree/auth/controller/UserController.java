package org.webtree.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.WTUserDetails;
import org.webtree.auth.service.UserAuthenticationService;


@RestController
@RequestMapping("/rest/user")
public class UserController<T extends UserDetails> extends AbstractController {

    private UserAuthenticationService service;

    @Autowired
    public UserController(UserAuthenticationService service) {
        this.service = service;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody AuthDetails authDetails) {
        int passwordLength = authDetails.getPassword().length();

        if (passwordLength != 128) { ///TODO: extracxt to validator
            return ResponseEntity.badRequest().body("The password must be a representation of sha512");
        }

        WTUserDetails userDetails = service.register(authDetails);
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.CREATED);
        responseEntity.getHeaders().add("id", userDetails.getId().toString());
        return responseEntity;
    }
}
