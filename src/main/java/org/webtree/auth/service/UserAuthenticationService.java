package org.webtree.auth.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.webtree.auth.AuthRepository;
import org.webtree.auth.domain.AuthDetails;


public class UserAuthenticationService<T extends UserDetails> implements UserDetailsService {

    private final AuthRepository<T> repository;

    public UserAuthenticationService(AuthRepository<T> repository) {
        this.repository = repository;
    }

    public boolean register(AuthDetails userDetails) {

        //todo main register operations
        return false;
    }


    public T loadUserByUsername(String s) throws UsernameNotFoundException {
        return repository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("User " + s + " not found"));
    }
}
