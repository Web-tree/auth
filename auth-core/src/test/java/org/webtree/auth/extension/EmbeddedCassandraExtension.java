package org.webtree.auth.extension;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EmbeddedCassandraExtension implements AfterTestExecutionCallback {


    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra("auth_main");
    }
}