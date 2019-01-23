package org.webtree.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.AuthRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String TEST_USERNAME = "Johnny";
    private static final String TOKEN = "token_1";

    @Mock
    private AuthRepository repository;
    @Mock
    private WtUserDetails wtUserDetails;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private AuthDetails authDetails;
    @Mock
    private WtUserDetailsFactory factory;
    @InjectMocks
    private AuthenticationServiceImpl service;

    @Test
    void shouldReturnUserByUsername() {
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(wtUserDetails));
        assertThat(service.loadUserByUsername(TEST_USERNAME)).isEqualTo(wtUserDetails);
    }

    @Test
    void shouldThrowExceptionIfUserWasNotFound() {
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        assertThatThrownBy(() -> service.loadUserByUsername(TEST_USERNAME))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void shouldReturnTokenWhenLogin() {
        given(authDetails.getUsername()).willReturn(TEST_USERNAME);
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(wtUserDetails));
        given(jwtTokenService.generateToken(wtUserDetails)).willReturn(TOKEN);
        assertThat(service.login(authDetails).getToken()).isEqualTo(TOKEN);
    }

    @Test
    void shouldThrowExceptionWhenUsernameWasNotFoundWhenLogin() {
        given(authDetails.getUsername()).willReturn(TEST_USERNAME);
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> service.login(authDetails)).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void shouldRegisterUser() {
        given(factory.createUserOf(authDetails)).willReturn(wtUserDetails);
        given(repository.saveIfNotExists(wtUserDetails)).willReturn(wtUserDetails);
        assertThat(service.register(authDetails)).isEqualTo(wtUserDetails);
    }
}