package org.webtree.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;

@Configuration("AuthPropertiesBean")
@ConfigurationProperties(prefix = "auth")
public class AuthConfigurationProperties {

    private final Route route = new Route();
    private final Jwt jwt = new Jwt();
    private final PasswordEncoder encoder = new PasswordEncoder();

    /**
     * Implementation of AuthDetails that is deserialized from request body from frontend.
     */
    private Class<? extends AuthDetails> authDetailsClass = AuthDetailsImpl.class;


    /**
     * Frontend origin for CORS policy.
     */
    private String frontendOrigin = "https://*.webtree.org";

    public Class<? extends AuthDetails> getAuthDetailsClass() {
        return authDetailsClass;
    }

    public void setAuthDetailsClass(Class<? extends AuthDetails> authDetailsClass) {
        this.authDetailsClass = authDetailsClass;
    }

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

    /**
     * Endpoints for authentication.
     */
    public static class Route {

        /**
         * Endpoint for register user.
         */
        private String register = "/rest/user/register";

        /**
         * Endpoint for user login.
         */
        private String login = "/rest/token/new";

        /**
         * Endpoint for login with social data.
         */
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

    /**
     * Json web token configuration.
     */
    public static class Jwt {

        /**
         * Header that must be in a request, where token is.
         */
        private String header = "Authorization";

        /**
         * Signing key for jwt token.
         */
        private String secret;

        /**
         * Token TTL before expire.
         */
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

    /**
     * Password encoder configuration.
     */
    public static class PasswordEncoder {

        /**
         * Salt, only even length , only hex characters(a-f) and numbers.
         */
        private String salt;

        /**
         * Password for password encoder, any value is suitable.
         */
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