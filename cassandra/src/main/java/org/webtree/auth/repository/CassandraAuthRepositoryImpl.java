package org.webtree.auth.repository;

import static org.springframework.data.cassandra.core.query.Criteria.where;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.webtree.auth.domain.WtUserDetails;

import java.util.Optional;

public class CassandraAuthRepositoryImpl implements CassandraAuthRepository {
    private CassandraOperations operations;
    private Class<? extends WtUserDetails> entityClass;

    public CassandraAuthRepositoryImpl(CassandraOperations operations, Class<? extends WtUserDetails> entityClass) {
        this.operations = operations;
        this.entityClass = entityClass;
    }

    @Override
    public Optional<WtUserDetails> findByUsername(String name) {
        return Optional.ofNullable(operations.selectOne(Query.query(where("username").is(name)), entityClass));
    }

    @Override
    public WtUserDetails saveIfNotExists(WtUserDetails e) {
        operations.insert(e);
        return e;
    }
}