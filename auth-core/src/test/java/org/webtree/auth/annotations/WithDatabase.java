package org.webtree.auth.annotations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, METHOD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@ContextConfiguration(initializers = WithDatabase.DbInit.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public @interface WithDatabase {

    JdbcDatabaseContainer SQL_CONTAINER = new MySQLContainer()
            .withDatabaseName("testDb")
            .withUsername("testUser")
            .withPassword("testPassword");

    class DbInit implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            SQL_CONTAINER.start();
            TestPropertyValues.of(
                    "spring.datasource.url=" + SQL_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + SQL_CONTAINER.getUsername(),
                    "spring.datasource.password=" + SQL_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
