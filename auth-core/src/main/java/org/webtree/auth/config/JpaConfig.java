package org.webtree.auth.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("org.webtree.auth.repository")
@EntityScan("org.webtree.auth.domain")
@EnableTransactionManagement
public class JpaConfig {
}
