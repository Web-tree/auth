package org.webtree.auth.rule;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.rules.ExternalResource;

public class ClearDBRule extends ExternalResource {

    @Override
    protected void after() {
        cleanKeyspaces();

    }

    private void cleanKeyspaces() {
        //TODO: clean all keyspaces
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra("trust_main");
    }
}
