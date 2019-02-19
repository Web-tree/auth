package org.webtree.auth.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.service.AuthenticationService;
import org.webtree.auth.service.AuthenticationServiceImpl;
import org.webtree.auth.service.JwtTokenService;
import org.webtree.auth.service.MappingWtUserDetailsFactory;
import org.webtree.auth.service.WtUserDetailsFactory;

@Configuration
@ComponentScan("org.webtree.auth")
public class AuthConfiguration {

    @Autowired
    private AuthConfigurationProperties properties;

    protected AuthConfigurationProperties getProperties() {
        return properties;
    }

    @Bean
    public AuthenticationService getService(AuthRepository repository, JwtTokenService services) {
        return new AuthenticationServiceImpl(services, repository, getFactory());
    }

    @Bean
    public WtUserDetailsFactory getFactory() {
        MappingWtUserDetailsFactory factory =
                new MappingWtUserDetailsFactory(new ModelMapper(), properties.getUserDetailsClass());
        return factory;
    }
}