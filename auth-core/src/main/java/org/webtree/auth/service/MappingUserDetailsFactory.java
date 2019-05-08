package org.webtree.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.User;

import javax.annotation.PostConstruct;

@Component
public class MappingUserDetailsFactory extends UserDetailsFactory {
    private ModelMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new ModelMapper();
    }

    @Override
    public User createUserOf(AuthDetails authDetails) {
        return mapper.map(authDetails, User.class);
    }
}