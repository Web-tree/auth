package org.webtree.auth.repository;

import org.webtree.auth.domain.WtUserDetails;

import java.util.Optional;

public interface AuthRepository {
    Optional<WtUserDetails> findByUsername(String name);

    WtUserDetails save(WtUserDetails e);
}
