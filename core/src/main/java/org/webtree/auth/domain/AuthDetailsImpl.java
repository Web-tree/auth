package org.webtree.auth.domain;

import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Objects;

public class AuthDetailsImpl implements Serializable, AuthDetails {
    private static final long serialVersionUID = 6425650941625914226L;
    private String username;

    @Size(min = 128, max = 128, message = "Password should be a representation of sha512")
    private String password;

    @ConstructorProperties({"username", "password"})
    public AuthDetailsImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDetailsImpl() {
    }

    public static AuthDetailsBuilder builder() {
        return new AuthDetailsBuilder();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthDetailsImpl)) return false;
        AuthDetailsImpl that = (AuthDetailsImpl) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public String toString() {
        return "AuthDetailsImpl{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static class AuthDetailsBuilder {
        private String username;
        private String password;

        AuthDetailsBuilder() {
        }

        public AuthDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuthDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthDetails build() {
            return new AuthDetailsImpl(username, password);
        }

        public String toString() {
            return "AuthDetailsImpl.AuthDetailsBuilder(username=" + this.username + ", password=" + this.password + ")";
        }
    }
}
