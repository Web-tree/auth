package org.webtree.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Table("wtuserdetailsimpl")
public class WTUserDetailsImpl implements WtUserDetails<String> {
    @Id
    private String id;
    @Indexed
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

    public WTUserDetailsImpl() {
    }

    @Override
    public String getId() {
        return id;
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
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WTUserDetailsImpl that = (WTUserDetailsImpl) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}