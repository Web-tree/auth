package org.webtree.auth.repository;


import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.auth.AbstractCassandraTest;
import org.webtree.auth.domain.WTUserDetailsImpl;
import org.webtree.auth.domain.WtUserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WtUserDetailsRepositoryTest extends AbstractCassandraTest {
    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";
    private static final String ID = "someId";

    @Autowired
    private CassandraAuthRepository repo;

    private WTUserDetailsImpl user;

    @Before
    public void setUp() {
        user = new WTUserDetailsImpl(ID, USERNAME, PASSWORD);
    }

    @Test
    public void shouldSaveAndFetchUser() {
        repo.save(user);
        Optional<WtUserDetails> details = repo.findByUsername(USERNAME);
        assertThat(user).isEqualTo(details.get());
    }

    @Test
    public void shouldFindUserByUserName() {
        WtUserDetails savedUser = repo.save(user);
        Optional<WtUserDetails> foundUser = repo.findByUsername(savedUser.getUsername());
        Assertions.assertThat(foundUser).isEqualTo(Optional.of(savedUser));
    }
}