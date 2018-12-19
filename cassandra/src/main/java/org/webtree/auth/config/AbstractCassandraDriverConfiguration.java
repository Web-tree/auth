package org.webtree.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.CassandraAuthRepository;
import org.webtree.auth.repository.CassandraAuthRepositoryImpl;

@Configuration
@EnableCassandraRepositories("org.webtree.auth.repository")
public abstract class AbstractCassandraDriverConfiguration  {
    public abstract Class<? extends WtUserDetails> getEntityClass();

    @Bean
    public CassandraAuthRepository getRepository(CassandraOperations operations) {
        CassandraAuthRepositoryImpl wtUserRepository = new CassandraAuthRepositoryImpl(operations);
        wtUserRepository.setEntityClass(getEntityClass());
        return wtUserRepository;
    }
}