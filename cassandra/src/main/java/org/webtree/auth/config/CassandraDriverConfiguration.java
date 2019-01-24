package org.webtree.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.webtree.auth.repository.CassandraAuthRepository;
import org.webtree.auth.repository.CassandraAuthRepositoryImpl;

@Configuration
@EnableCassandraRepositories("org.webtree.auth.repository")
public class CassandraDriverConfiguration extends AuthConfiguration {

    @Bean
    public CassandraAuthRepository getRepository(CassandraOperations operations) {
        CassandraAuthRepositoryImpl wtUserRepository = new CassandraAuthRepositoryImpl(operations, getEntityClass());
        return wtUserRepository;
    }


    //TODO check index on username
}