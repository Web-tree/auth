package org.webtree.auth.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.service.UserAuthenticationService;
import org.webtree.auth.service.UserAuthenticationServiceImpl;

@Configuration
@ComponentScan("org.webtree.auth")
public abstract class AbstractAuthConfiguration {

    @Bean
    protected UserAuthenticationService userAuthenticationServiceBean(AuthRepository repository) {
        UserAuthenticationService service = new UserAuthenticationServiceImpl(repository);
        configureUserServiceBean(service);
        return service;
    }


    abstract void configureUserServiceBean(UserAuthenticationService impl);
}