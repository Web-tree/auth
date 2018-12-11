package org.webtree.auth.repository;

import org.webtree.auth.domain.WTUserDetails;

import java.util.Optional;

public interface AuthRepository {
    Optional<WTUserDetails> findByUsername(String name);

    WTUserDetails save(WTUserDetails e);
}
