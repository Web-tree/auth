package org.webtree.auth.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.webtree.auth.domain.UserLock;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Udjin Skobelev on 16.08.2018.
 */

public class UserLockRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private CassandraOperations operations;

    @Autowired
    private UserLockRepository repo;

    private UserLock user;

    @BeforeEach
    public void setUp() {
        user = new UserLock("someUserName");
    }

    @AfterEach
    public void tearDown() {
        operations.truncate(UserLock.class);
    }

    @Test
    public void shouldReturnTrueIfNotExists() {
        assertThat(repo.saveIfNotExist(user)).isTrue();
    }

    @Test
    public void shouldFindIfExists() {
        assertThat(repo.findById(user.getUsername()).isPresent()).isFalse();
        repo.saveIfNotExist(user);
        assertThat(repo.findById(user.getUsername()).isPresent()).isTrue();
    }

    @Test
    public void shouldReturnFalseIfAlreadyExists() {
        repo.saveIfNotExist(user);
        assertThat(repo.saveIfNotExist(user)).isFalse();
    }

    @Test
    public void shouldDeleteLock() {
        repo.saveIfNotExist(user);
        repo.delete(user);
        assertThat(repo.findById(user.getUsername()).isPresent()).isFalse();
    }
}