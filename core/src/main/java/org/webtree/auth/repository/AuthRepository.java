package org.webtree.auth.repository;

import org.webtree.auth.domain.User;

import java.util.Optional;

public interface AuthRepository  {
    Optional<User> findByUsername(String name);

    User saveIfNotExists(User user);
}
