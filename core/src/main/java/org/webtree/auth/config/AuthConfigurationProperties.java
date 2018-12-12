package org.webtree.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "auth", ignoreUnknownFields = true)
public class AuthConfigurationProperties {
    private String registerUrl = "/rest/user/register";
    private String newTokenUrl = "/rest/token/new";
    private String socialLoginUrl = "/rest/social/login";

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getNewTokenUrl() {
        return newTokenUrl;
    }

    public void setNewTokenUrl(String newTokenUrl) {
        this.newTokenUrl = newTokenUrl;
    }

    public String getSocialLoginUrl() {
        return socialLoginUrl;
    }

    public void setSocialLoginUrl(String socialLoginUrl) {
        this.socialLoginUrl = socialLoginUrl;
    }
}
