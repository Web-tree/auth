package org.webtree.auth.domain;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

/**
 * Created by Udjin Skobelev on 16.08.2018.
 */

@Table("username_lock")
public class UserLock {
    @PrimaryKey
    private String username;

    public UserLock(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLock)) return false;
        UserLock userLock = (UserLock) o;
        return Objects.equals(username, userLock.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
