package org.webtree.auth.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.webtree.auth.deserializer.EncodePasswordDeserializer;

import java.io.Serializable;
import java.util.Objects;

public class AuthDetails implements Serializable {
    private static final long serialVersionUID = 6425650941625914226L;
    private String username;
    private String password;

    public AuthDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Used by spring
    public AuthDetails() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonDeserialize(using = EncodePasswordDeserializer.class)
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
}