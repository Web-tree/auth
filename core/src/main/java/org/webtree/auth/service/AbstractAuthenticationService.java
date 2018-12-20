package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.AuthRepository;

public abstract class AbstractAuthenticationService implements AuthenticationService {
    private AuthRepository repository;

    public AbstractAuthenticationService(AuthRepository repository) {
        this.repository = repository;
    }

    @Override
    public WtUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }
}