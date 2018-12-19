package org.webtree.auth.repository;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;
import org.webtree.auth.domain.WtUserDetails;

import java.util.Optional;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@Repository
public class CassandraAuthRepositoryImpl implements CassandraAuthRepository {
    private CassandraOperations operations;
    private Class<? extends WtUserDetails> aClass;

    public void setEntityClass(Class<? extends WtUserDetails> aClass) {
        this.aClass = aClass;
    }

    public CassandraAuthRepositoryImpl(CassandraOperations operations) {
        this.operations = operations;
    }

    @Override
    public Optional<WtUserDetails> findByUsername(String name) {
        return Optional.ofNullable(operations.selectOne(Query.query(where("username").is(name)), aClass));
    }

    @Override
    public WtUserDetails save(WtUserDetails e) {
        operations.insert(e);
        return e;
    }
}