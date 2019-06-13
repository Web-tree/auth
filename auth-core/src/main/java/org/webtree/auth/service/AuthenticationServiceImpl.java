package org.webtree.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.User;
import org.webtree.auth.repository.AuthRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private JwtTokenService jwtTokenService;
    private AuthRepository repository;

    @Autowired
    public AuthenticationServiceImpl(JwtTokenService jwtTokenService, AuthRepository repository) {
        this.jwtTokenService = jwtTokenService;
        this.repository = repository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public User decodeToken(String token) {
        if (!jwtTokenService.isTokenValid(token)) {
            throw new JwtTokenService.InvalidTokenException();
        }
        return User.builder()
                .withUsername(jwtTokenService.getUsernameFromToken(token))
                .build();
    }

    @Override
    @Transactional
    public User register(AuthDetails authDetails) {
        repository.findByUsername(authDetails.getUsername()).ifPresent(u -> {throw new UserAlreadyRegistered();});
        return repository.save(User.builder()
                .withUsername(authDetails.getUsername())
                .withPassword(authDetails.getPassword())
                .build()
        );
    }

    @Override
    public Token login(AuthDetails authDetails) {
        User user = repository.findByUsername(
                authDetails.getUsername()).orElseThrow(() -> new BadCredentialsException("Wrong password or username"));
        return () -> jwtTokenService.generateToken(user);
    }
}