package org.webtree.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.WtUserDetails;

@Configuration("AuthPropertiesBean")
@ConfigurationProperties(prefix = "auth")
public class AuthConfigurationProperties {
    private final Route route = new Route();
    private final Jwt jwt = new Jwt();
    private final PasswordEncoder encoder = new PasswordEncoder();
    private Class<? extends AuthDetails> authDetailsClass = AuthDetailsImpl.class;
    private Class<? extends WtUserDetails> userDetailsClass;

    public Class<? extends WtUserDetails> getUserDetailsClass() {
        return userDetailsClass;
    }

    public void setUserDetailsClass(Class<? extends WtUserDetails> userDetailsClass) {
        this.userDetailsClass = userDetailsClass;
    }

    public Class<? extends AuthDetails> getAuthDetailsClass() {
        return authDetailsClass;
    }

    public void setAuthDetailsClass(Class<? extends AuthDetails> authDetailsClass) {
        this.authDetailsClass = authDetailsClass;
    }

    private String frontendOrigin = "https://*.webtree.org";

    public String getFrontendOrigin() {
        return frontendOrigin;
    }

    public void setFrontendOrigin(String frontendOrigin) {
        this.frontendOrigin = frontendOrigin;
    }

    public Route getRoute() {
        return route;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public PasswordEncoder getEncoder() {
        return encoder;
    }

    public static class Route {
        private String register = "/rest/user/register";
        private String login = "/rest/token/new";
        private String socialLogin = "/rest/social/login";

        public String getRegister() {
            return register;
        }

        public void setRegister(String register) {
            this.register = register;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getSocialLogin() {
            return socialLogin;
        }

        public void setSocialLogin(String socialLogin) {
            this.socialLogin = socialLogin;
        }
    }

    public static class Jwt {
        private String header = "Authorization";
        private String secret;
        private Long expiration = 604800L;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Long getExpiration() {
            return expiration;
        }

        public void setExpiration(Long expiration) {
            this.expiration = expiration;
        }
    }

    public static class PasswordEncoder {
        private String salt;
        private String password;

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}