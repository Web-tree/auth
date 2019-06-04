package org.webtree.auth.api.model;

public class UsernameResponse {
    private String username;

    public UsernameResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}