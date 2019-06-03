package org.webtree.auth.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.auth.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AuthRepositoryTest extends AbstractCassandraTest {
    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";
    private static final String ID = "someId";

    @Autowired
    private AuthRepository repo;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.newBuilder().withPassword(PASSWORD).withUsername(USERNAME).build();
    }

    @Test
    void shouldSaveAndFetchUser() {
        repo.saveIfNotExists(user);
        Optional<User> details = repo.findByUsername(USERNAME);
        assertThat(details.isPresent()).isTrue();
    }
}