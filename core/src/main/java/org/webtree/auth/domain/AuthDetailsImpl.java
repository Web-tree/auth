package org.webtree.auth.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Size;

public class AuthDetailsImpl implements Serializable, AuthDetails {
    private static final long serialVersionUID = 6425650941625914226L;
    private String username;

    @Size(min = 128, max = 128, message = "Password should be a representation of sha512")
    private String password;

    public AuthDetailsImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDetailsImpl() {
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthDetailsImpl that = (AuthDetailsImpl) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "AuthDetailsImpl{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}