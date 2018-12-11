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


@RestController
@RequestMapping("/rest/user")
public class UserController<T extends UserDetails> extends AbstractController {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody AuthDetails authDetails) {
        int passwordLength = authDetails.getPassword().length();

        if (passwordLength != 128) {
            return ResponseEntity.badRequest().body("The password must be a representation of sha512");
        }

        if (service.register(authDetails)) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body("User with this username already exists.");
        }
    }
}
