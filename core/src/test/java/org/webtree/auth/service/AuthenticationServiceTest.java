package org.webtree.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.AuthRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private static final String TEST_USERNAME = "Johnny";

    @Mock
    private AuthRepository repository;
    @Mock
    private WtUserDetails details;
    private AbstractAuthenticationService service;

    @BeforeEach
    void setUp() {
        service = mock(AbstractAuthenticationService.class,
                withSettings().defaultAnswer(Answers.CALLS_REAL_METHODS).useConstructor(repository));
    }

    @Test
    public void shouldReturnUserByUsername() {
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(details));
        assertThat(service.loadUserByUsername(TEST_USERNAME)).isEqualTo(details);
    }

    @Test
    public void shouldThrowExceptionIfUserWasNotFound() {
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.loadUserByUsername(TEST_USERNAME))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}