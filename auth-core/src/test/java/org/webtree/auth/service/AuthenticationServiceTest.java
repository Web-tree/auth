package org.webtree.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.User;
import org.webtree.auth.domain.UserLock;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.repository.UserLockRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String TEST_USERNAME = "Johnny";
    private static final String TOKEN = "token_1";

    @Mock
    private AuthRepository repository;

    @Mock
    private UserLockRepository lockRepository;

    private User user;

    @Mock
    private JwtTokenService jwtTokenService;

    private AuthDetails authDetails;

    @Mock
    private UserDetailsFactory factory;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @BeforeEach
    void setUp() {
        authDetails = new AuthDetailsImpl("name", "pass");
        user = new User("", "u", "p", null);
    }

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
        given(repository.findByUsername(anyString())).willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> service.login(authDetails)).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    public void shouldReturnFalseIfUserDoNotExistButCantMakeLock() {
        given(factory.createUserOf(authDetails)).willReturn(user);
        given(repository.findByUsername(anyString())).willReturn(Optional.empty());
        given(lockRepository.saveIfNotExist(any(UserLock.class))).willReturn(false);
        assertThat(service.registerIfNotExists(authDetails)).isFalse();
        verify(repository, never()).saveIfNotExists(any(User.class));
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        given(factory.createUserOf(authDetails)).willReturn(user);
        given(repository.findByUsername(anyString())).willReturn(Optional.of(user));
        assertThat(service.registerIfNotExists(authDetails)).isFalse();
        verifyNoMoreInteractions(lockRepository);
        verify(repository, never()).saveIfNotExists(any(User.class));
    }

    @Test
    public void shouldReturnTrueIfUserDoesNotExists() {
        given(factory.createUserOf(authDetails)).willReturn(user);
        given(repository.findByUsername(anyString())).willReturn(Optional.empty());
        given(lockRepository.saveIfNotExist(any(UserLock.class))).willReturn(true);
        assertThat(service.registerIfNotExists(authDetails)).isTrue();
    }

    @Test
    void shouldReturnTrueIfTokenISCorrect(){
        String someToken = "someToken";
        given(jwtTokenService.getUsernameFromToken(someToken)).willReturn(user.getUsername());
        given(repository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(jwtTokenService.validateToken(someToken,user)).willReturn(true);

        assertThat(service.checkToken(someToken)).isTrue();
    }

    @Test
    void shouldReturnFalseIfTokenIsNotCorrect(){
        String someToken = "someToken";
        given(jwtTokenService.getUsernameFromToken(someToken)).willReturn(user.getUsername());
        given(repository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(jwtTokenService.validateToken(someToken,user)).willReturn(true);

        assertThat(service.checkToken(someToken)).isTrue();
    }

    @Test
    void shouldReturnFalseIfCantFindUserByNameFromToken() {
        String someToken = "someToken";
        given(jwtTokenService.getUsernameFromToken(someToken)).willReturn(user.getUsername());
        given(repository.findByUsername(anyString())).willReturn(Optional.empty());

        assertThat(service.checkToken(someToken)).isFalse();
    }
}