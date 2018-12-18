package org.webtree.auth.domain;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

public class WTUserDetailsImpl implements WtUserDetails<String> {

    private String id;
    private String username;
    private String password;

    public WTUserDetailsImpl(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public WTUserDetailsImpl(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Date getLastPasswordResetDate() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}