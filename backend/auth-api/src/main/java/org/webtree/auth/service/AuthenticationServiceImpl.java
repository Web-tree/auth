package org.webtree.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.User;
import org.webtree.auth.exception.BadCredentialsException;
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
    public User decodeToken(String token) {
        if (!jwtTokenService.isTokenValid(token)) {
            throw new JwtTokenService.InvalidTokenException();
        }
        return User.builder()
                .withUsername(jwtTokenService.getUsernameFromToken(token))
                .withId(jwtTokenService.getIdFromToken(token))
                .build();
    }

    @Override
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
        return new Token(jwtTokenService.generateToken(user));
    }
}
