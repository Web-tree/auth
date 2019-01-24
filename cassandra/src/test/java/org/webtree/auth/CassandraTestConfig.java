package org.webtree.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.webtree.auth.domain.WTUserDetailsImpl;
import org.webtree.auth.repository.CassandraAuthRepositoryImpl;

import java.util.Collections;
import java.util.List;

@SpringBootConfiguration
public class CassandraTestConfig {

    @Bean
    public CassandraAuthRepositoryImpl getRepository(CassandraOperations operations) {
        return new CassandraAuthRepositoryImpl(operations, WTUserDetailsImpl.class);
    }

    @Configuration
    @EnableCassandraRepositories("org.webtree.auth.repository")
    public class EmbeddedCassandraConfig extends AbstractCassandraConfiguration {
        private static final String ENTITY_BASE_PACKAGE = "org.webtree.auth.domain";
        @Value("${spring.data.cassandra.port}")
        private int port;
        @Value("${spring.data.cassandra.contact-points}")
        private String contactPoints;
        @Value("${spring.data.cassandra.keyspace-name}")
        private String keySpace;

        @Override
        public String[] getEntityBasePackages() {
            return new String[]{ENTITY_BASE_PACKAGE};
        }

        @Override
        public SchemaAction getSchemaAction() {
            return SchemaAction.CREATE_IF_NOT_EXISTS;
        }

        @Override
        protected String getKeyspaceName() {
            return keySpace;
        }

        @Override
        protected String getContactPoints() {
            return contactPoints;
        }

        protected int getPort() {
            return port;
        }

        @Override
        protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
            CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keySpace)
                    .with(KeyspaceOption.DURABLE_WRITES, true)
                    .ifNotExists()
                    .withSimpleReplication(1L);
            return Collections.singletonList(specification);
        }
    }
}