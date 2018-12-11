package org.webtree.auth.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.webtree.auth.AuthRepository;
import org.webtree.auth.service.UserAuthenticationService;

@Configuration
@ComponentScan("org.webtree.auth")
public abstract class AbstractAuthConfiguration {

    @Bean
    protected UserAuthenticationService<? extends UserDetails> userAuthenticationServiceBean(AuthRepository<? extends UserDetails> repository) {
        UserAuthenticationService<> service = new UserAuthenticationService(repository);
        configureUserServiceBean(service);
        return service;
    }


    abstract void configureUserServiceBean(UserAuthenticationService impl);
}