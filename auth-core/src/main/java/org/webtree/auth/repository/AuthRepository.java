package org.webtree.auth.repository;

import org.webtree.auth.domain.User;

import java.util.Optional;

public interface AuthRepository  {
    Optional<User> findByUsername(String name);

    @Deprecated
    User saveIfNotExists(User user);

    User save(User user);
}
