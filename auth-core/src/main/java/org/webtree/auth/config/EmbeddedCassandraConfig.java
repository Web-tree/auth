package org.webtree.auth.config;

import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

import java.io.IOException;

/**
 * Created by Udjin Skobelev on 03.12.2018.
 */

@Profile("dev")
@Configuration
public class EmbeddedCassandraConfig extends CassandraAuthConfiguration {
    private static final String MESSAGE = "Cant start embedded cassandra server";

    @Override
    @Bean
    public CassandraClusterFactoryBean cluster() {
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000L);
        } catch (TTransportException | IOException e) {
            throw new RuntimeException(MESSAGE, e);
        }
        return super.cluster();
    }
}