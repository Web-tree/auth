package org.webtree.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.AuthRepository;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenService jwtTokenService;
    private AuthRepository repository;
    private ModelMapper modelMapper;
    private Class<? extends WtUserDetails> entityClass;

    public void setEntityClass(Class<? extends WtUserDetails> entityClass) {
        this.entityClass = entityClass;
    }

    public AuthenticationServiceImpl(AuthRepository repository, JwtTokenService service, ModelMapper modelMapper) {
        this.jwtTokenService = service;
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public WtUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return repository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }

    @Override
    public Token login(AuthDetails authDetails) {
        WtUserDetails userDetails = repository.findByUsername(
                authDetails.getUsername()).orElseThrow(() -> new BadCredentialsException("Wrong password or username"));
        return () -> jwtTokenService.generateToken(userDetails);
    }

    @Override
    public WtUserDetails register(AuthDetails authDetails) {
        WtUserDetails userDetails = modelMapper.map(authDetails, entityClass);
        return repository.saveIfNotExists(userDetails);
    }
}