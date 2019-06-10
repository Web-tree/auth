package org.webtree.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.User;
import org.webtree.auth.domain.UserLock;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.repository.UserLockRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private JwtTokenService jwtTokenService;
    private AuthRepository repository;
    private UserDetailsFactory factory;
    private UserLockRepository lockRepository;

    @Autowired
    public AuthenticationServiceImpl(JwtTokenService jwtTokenService,
                                     AuthRepository repository,
                                     UserDetailsFactory factory,
                                     UserLockRepository lockRepository) {
        this.jwtTokenService = jwtTokenService;
        this.repository = repository;
        this.factory = factory;
        this.lockRepository = lockRepository;
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
    public void register(AuthDetails authDetails) {
        repository.save(User.builder()
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

    @Override
    public boolean registerIfNotExists(AuthDetails authDetails) {
        User user = factory.createUserOf(authDetails);

        if (existsByUsername(user.getUsername())) {
            return false;
        }

        UserLock lock = new UserLock(user.getUsername());

        if (lockRepository.saveIfNotExist(lock)) {
            try {
                if (existsByUsername(user.getUsername())) { // TODO: cover by test
                    return false;
                } else {
                    repository.saveIfNotExists(user);
                }
            } finally {
                lockRepository.delete(lock);
            }
            return true;
        }
        return false;
    }

    private boolean existsByUsername(String name) {
        return repository.findByUsername(name).isPresent();
    }

}