package org.webtree.auth.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(SpringExtension.class)

public class DatabaseExtension implements AfterEachCallback {

    private static JdbcDatabaseContainer SQL_CONTAINER = new MySQLContainer()
            .withDatabaseName("testDb")
            .withUsername("testUser")
            .withPassword("testPassword");
    private static AtomicReference<ApplicationContext> contextCache = new AtomicReference<>();

    public static class DbInit implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            SQL_CONTAINER.start();
            TestPropertyValues.of(
                    "spring.datasource.url=" + SQL_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + SQL_CONTAINER.getUsername(),
                    "spring.datasource.password=" + SQL_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
            contextCache.set(configurableApplicationContext);
        }
    }

    @Override
    @Transactional
    public void afterEach(ExtensionContext extensionContext) {

        Map<String, JpaRepository> repositoryMap = contextCache.get().getBeansOfType(JpaRepository.class);
        repositoryMap.values().forEach(CrudRepository::deleteAll);
    }

}
