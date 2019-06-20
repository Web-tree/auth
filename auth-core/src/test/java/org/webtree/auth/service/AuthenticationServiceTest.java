package org.webtree.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.User;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.service.JwtTokenService.InvalidTokenException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String TEST_USERNAME = "Johnny";
    private static final String TOKEN = "token_1";
    private static final String TEST_PASSWORD = "pass";

    @Mock
    private AuthRepository repository;

    private User user;

    @Mock
    private JwtTokenService jwtTokenService;

    private AuthDetails authDetails;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
        authDetails = new AuthDetails(TEST_USERNAME, TEST_PASSWORD);
        user = User.builder().withUsername("u").withPassword("p").build();
    }

    @Nested class FindTests {
        @Test
        void shouldReturnUserByUsername() {
            given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(user));
            assertThat(service.loadUserByUsername(TEST_USERNAME)).isEqualTo(user);
        }

        @Test
        void shouldThrowExceptionIfUserWasNotFound() {
            given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
            assertThatThrownBy(() -> service.loadUserByUsername(TEST_USERNAME))
                    .isInstanceOf(UsernameNotFoundException.class);
        }

        @Test
        void shouldReturnTokenWhenLogin() {
            given(repository.findByUsername(anyString())).willReturn(Optional.of(user));
            given(jwtTokenService.generateToken(user)).willReturn(TOKEN);
            assertThat(service.login(authDetails).getToken()).isEqualTo(TOKEN);
        }

        @Test
        void shouldThrowExceptionWhenUsernameWasNotFoundWhenLogin() {
            given(repository.findByUsername(anyString())).willReturn(Optional.empty());
            assertThatThrownBy(() -> service.login(authDetails)).isInstanceOf(BadCredentialsException.class);
        }

    }

    @Nested class RegisterTests {

        @Test
        void shouldPropagateRegistrationToRepository() {
            service.register(authDetails);

            verify(repository).save(userCaptor.capture());

            User user = userCaptor.getValue();
            assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
            assertThat(user.getPassword()).isEqualTo(TEST_PASSWORD);
        }

        @Test
        void shouldThrowExceptionWhenUserAlreadyExists() {
            when(repository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(User.builder().build()));

            assertThatThrownBy(() -> service.register(authDetails)).isInstanceOf(AuthenticationService.UserAlreadyRegistered.class);
        }
    }

    @Nested class TokenTests {

        @Test
        void shouldDecodeUserFromToken() {
            String someToken = "someToken";
            String username = "someUsername";
            given(jwtTokenService.isTokenValid(someToken)).willReturn(true);
            given(jwtTokenService.getUsernameFromToken(someToken)).willReturn(username);

            assertThat(service.decodeToken(someToken).getUsername()).isEqualTo(username);
        }

        @Test
        void shouldThrowExceptionIfTokenIsNotCorrect() {
            String someToken = "someToken";
            given(jwtTokenService.isTokenValid(someToken)).willReturn(false);

            assertThatThrownBy(() -> service.decodeToken(someToken)).isInstanceOf(InvalidTokenException.class);
        }

    }

}