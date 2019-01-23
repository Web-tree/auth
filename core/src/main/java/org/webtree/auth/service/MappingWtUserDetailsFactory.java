package org.webtree.auth.service;

import org.modelmapper.ModelMapper;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.WtUserDetails;

public class MappingWtUserDetailsFactory extends WtUserDetailsFactory {
    private ModelMapper mapper;
    private Class<? extends WtUserDetails> entityClass;

    public MappingWtUserDetailsFactory(ModelMapper mapper, Class<? extends WtUserDetails> entityClass) {
        this.mapper = mapper;
        this.entityClass = entityClass;
    }

    @Override
    public WtUserDetails createUserOf(AuthDetails authDetails) {
        return mapper.map(authDetails, entityClass);
    }
}