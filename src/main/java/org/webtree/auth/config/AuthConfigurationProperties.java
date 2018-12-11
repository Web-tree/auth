package org.webtree.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "server",
        ignoreUnknownFields = true)
public class AuthConfigurationProperties {
}
