package org.webtree.auth.domain;

import org.springframework.security.core.userdetails.UserDetails;

public interface WTUserDetails<T> extends UserDetails {
    T getId();

    String getUsername();
}
