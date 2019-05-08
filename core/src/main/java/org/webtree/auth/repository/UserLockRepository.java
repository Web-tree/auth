package org.webtree.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.stereotype.Repository;
import org.webtree.auth.domain.UserLock;

import java.util.Optional;

import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

/**
 * Created by Udjin Skobelev on 16.08.2018.
 */
@Repository
public class UserLockRepository {
    private CassandraOperations operations;

    @Autowired
    public UserLockRepository(CassandraOperations operations) {
        this.operations = operations;
    }

    public boolean saveIfNotExist(UserLock lock) {
        return operations.insert(lock, InsertOptions.builder().withIfNotExists().ttl(60 * 10).build()).wasApplied();
    }

    public void delete(UserLock lock) {
        operations.delete(lock);
    }

    public Optional<UserLock> findById(String username) {
        return Optional.ofNullable(operations.selectOne(query(where("username").is(username)), UserLock.class));
    }
}