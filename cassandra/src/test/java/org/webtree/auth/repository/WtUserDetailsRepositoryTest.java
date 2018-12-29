package org.webtree.auth.repository;


import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.webtree.auth.domain.WTUserDetailsImpl;
import org.webtree.auth.domain.WtUserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

 class WtUserDetailsRepositoryTest extends AbstractCassandraTest {
    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";
    private static final String ID = "someId";

    @Autowired
    private CassandraAuthRepository repo;

    private WTUserDetailsImpl user;

    @BeforeEach
     void setUp() {
        user = new WTUserDetailsImpl(ID, USERNAME, PASSWORD);
    }

    @Test
     void shouldSaveAndFetchUser() {
        repo.saveIfNotExists(user);
        Optional<WtUserDetails> details = repo.findByUsername(USERNAME);
        assertThat(user).isEqualTo(details.get());
    }

    @Test
     void shouldFindUserByUserName() {
        WtUserDetails savedUser = repo.saveIfNotExists(user);
        Optional<WtUserDetails> foundUser = repo.findByUsername(savedUser.getUsername());
        Assertions.assertThat(foundUser).isEqualTo(Optional.of(savedUser));
    }
}