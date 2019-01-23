package org.webtree.auth.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.service.AuthenticationService;
import org.webtree.auth.service.AuthenticationServiceImpl;
import org.webtree.auth.service.JwtTokenService;
import org.webtree.auth.service.MappingWtUserDetailsFactory;
import org.webtree.auth.service.WtUserDetailsFactory;

@Configuration
@ComponentScan("org.webtree.auth")
public class AuthConfiguration {

    @Value("${auth.wt-user-impl}")
    private Class<? extends WtUserDetails> entityClass;

    protected Class<? extends WtUserDetails> getEntityClass() {
        return entityClass;
    }

    @Bean
    public AuthenticationService getService(AuthRepository repository, JwtTokenService services) {
        return new AuthenticationServiceImpl(services,repository,getFactory());
    }

    @Bean
    public WtUserDetailsFactory getFactory(){
        MappingWtUserDetailsFactory factory = new MappingWtUserDetailsFactory(new ModelMapper(),getEntityClass());
        return factory;
    }
}