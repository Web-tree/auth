package org.webtree.auth.extension;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EmbeddedCassandraExtension implements BeforeAllCallback, AfterTestExecutionCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000L);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra("auth_main");
    }
}