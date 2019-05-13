package org.webtree.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ComponentScan("org.webtree.auth")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthDetailsRequestBodyProcessor passwordProcessor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(passwordProcessor);
    }

    //todo запускать спринг бут
}