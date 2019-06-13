package org.webtree.auth.repository;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.auth.annotations.WithDatabase;
import org.webtree.auth.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@WithDatabase
class AuthRepositoryTest {
    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";
    @Autowired
    private AuthRepository repo;

    @Nested
    class Save {
        private static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

        @Test
        void shouldGenerateId() {
            User user = repo.save(buildStandardUser());
            assertThat(user.getId()).matches(UUID_PATTERN);
        }

        @Test
        void shouldSaveUser() {
            User user = buildStandardUser();
            User saved = repo.save(user);
            assertThat(saved.getUsername()).isEqualTo(USERNAME);
            assertThat(saved.getPassword()).isEqualTo(PASSWORD);
        }
    }

    @NotNull private User buildStandardUser() {
        return User.builder().withUsername(USERNAME).withPassword(PASSWORD).build();
    }

    @Nested
    class Find {
        @Test
        void shouldFindUserByName() {
            repo.save(buildStandardUser());

            Optional<User> user = repo.findByUsername(USERNAME);
            assertThat(user).isPresent();
            assertThat(user.get().getUsername()).isEqualTo(USERNAME);
            assertThat(user.get().getPassword()).isEqualTo(PASSWORD);
        }
    }
}