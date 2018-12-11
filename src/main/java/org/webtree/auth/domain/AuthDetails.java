package org.webtree.auth.domain;

import java.io.Serializable;
import java.util.Objects;


public class AuthDetails implements Serializable {
    private static final long serialVersionUID = 6425650941625914226L;
    private String username;
    private String password;

    @java.beans.ConstructorProperties({"username", "password"})
    public AuthDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDetails() {
    }

    public static AuthDetailsBuilder builder() {
        return new AuthDetailsBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthDetails)) return false;
        AuthDetails that = (AuthDetails) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "AuthDetails{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static class AuthDetailsBuilder {
        private String username;
        private String password;

        AuthDetailsBuilder() {
        }

        public AuthDetails.AuthDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuthDetails.AuthDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthDetails build() {
            return new AuthDetails(username, password);
        }

        public String toString() {
            return "AuthDetails.AuthDetailsBuilder(username=" + this.username + ", password=" + this.password + ")";
        }
    }
}
