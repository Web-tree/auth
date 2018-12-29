package org.webtree.auth.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.webtree.auth.deserializer.EncodePasswordDeserializer;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Size;

public class AuthDetailsImpl implements Serializable, AuthDetails {
    private static final long serialVersionUID = 6425650941625914226L;
    private String username;
    private String password;

    public AuthDetailsImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDetailsImpl() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonDeserialize(using = EncodePasswordDeserializer.class)
    public void setPassword(String password) {
        this.password = password;
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