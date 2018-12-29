package org.webtree.auth;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webtree.auth.extension.EmbeddedCassandraExtension;

@ExtendWith({SpringExtension.class, EmbeddedCassandraExtension.class})
@SpringBootTest(classes = CassandraTestConfig.class)
@ActiveProfiles("cassandra-test")
public abstract class AbstractCassandraTest {
}