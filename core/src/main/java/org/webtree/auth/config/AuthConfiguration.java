package org.webtree.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.service.AuthenticationServiceImpl;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan("org.webtree.auth")
public class AuthConfiguration {

    @Value("${auth.wt-user-impl}")
    private Class<? extends WtUserDetails> entity;

    protected Class<? extends WtUserDetails> getEntityClass() {
        return entity;
    }

    @Autowired
    private AuthenticationServiceImpl service;

    @PostConstruct
    public void init() {
        configAuthService();
    }

    private void configAuthService() {
        service.setEntityClass(entity);
    }
}