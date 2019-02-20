package org.webtree.auth.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.AuthRepository;

public class AuthenticationServiceImpl implements AuthenticationService {
    private JwtTokenService jwtTokenService;
    private AuthRepository repository;
    private WtUserDetailsFactory factory;

    public AuthenticationServiceImpl(JwtTokenService jwtTokenService,
                                     AuthRepository repository,
                                     WtUserDetailsFactory factory) {
        this.jwtTokenService = jwtTokenService;
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public WtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Token login(AuthDetails authDetails) {
        WtUserDetails userDetails = repository.findByUsername(
                authDetails.getUsername()).orElseThrow(() -> new BadCredentialsException("Wrong password or username"));
        return () -> jwtTokenService.generateToken(userDetails);
    }

    @Override
    public WtUserDetails register(AuthDetails authDetails) {
        WtUserDetails userDetails = factory.createUserOf(authDetails);
        return repository.saveIfNotExists(userDetails);
    }
}