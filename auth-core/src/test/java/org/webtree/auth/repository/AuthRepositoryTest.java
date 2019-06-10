package org.webtree.auth.repository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.auth.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class AuthRepositoryTest extends AbstractSqlTest {
    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";
    private static final String ID = "someId";
    @Autowired
    private AuthRepository repo;

    @Nested
    class SaveTests {
        private static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

        @Test
        void shouldGenerateId() {
            User user = repo.save(User.builder().withUsername(USERNAME).withPassword(PASSWORD).build());
            assertThat(user.getId()).matches(UUID_PATTERN);
        }

        @Test
        void shouldSaveUser() {
            User user = User.builder().withUsername(USERNAME).withPassword(PASSWORD).build();
            User saved = repo.save(user);
            assertThat(saved.getUsername()).isEqualTo(USERNAME);
            assertThat(saved.getPassword()).isEqualTo(PASSWORD);
        }
    }
}