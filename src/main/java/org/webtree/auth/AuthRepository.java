package org.webtree.auth;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthRepository<T extends UserDetails> {
    Optional<T> findByUsername(String name);

    T save(T e);
}
