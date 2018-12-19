package org.webtree.auth.rule;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.rules.ExternalResource;

public class CassandraTestRule extends ExternalResource {
    @Override
    protected void before() throws Throwable {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000L);
    }
}
