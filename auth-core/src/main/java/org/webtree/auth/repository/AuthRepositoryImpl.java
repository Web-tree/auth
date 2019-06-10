package org.webtree.auth.repository;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webtree.auth.domain.User;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@Repository
public class AuthRepositoryImpl implements AuthRepository {
    private final JpaRepository<User, String> userRepository;
    private CassandraOperations operations;

    @Autowired
    public AuthRepositoryImpl(CassandraOperations operations, UserRepository userRepository) {
        this.operations = operations;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String name) {
        return Optional.ofNullable(operations.selectOne(Query.query(where("username").is(name)), User.class));
    }

    @Override
    public User saveIfNotExists(User user) {
        operations.getCqlOperations()
                .execute(QueryBuilder
                        .insertInto(operations.getTableName(user.getClass()).getUnquoted())
                        .value("id", UUID.randomUUID().toString())
                        .value("password", user.getPassword())
                        .value("username", user.getUsername())
                        .ifNotExists());
        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}