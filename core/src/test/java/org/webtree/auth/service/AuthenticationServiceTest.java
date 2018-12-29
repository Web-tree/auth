package org.webtree.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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
    private WtUserDetails details;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private AuthDetails authDetails;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private AuthenticationServiceImpl service;

    @BeforeEach
    void setUp() {
        service.setEntityClass(WtUserDetails.class);
    }

    @Test
    void shouldReturnUserByUsername() {
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(details));
        assertThat(service.loadUserByUsername(TEST_USERNAME)).isEqualTo(details);
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
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(details));
        given(jwtTokenService.generateToken(details)).willReturn(TOKEN);
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
        given(mapper.map(authDetails, WtUserDetails.class)).willReturn(details);
        given(repository.saveIfNotExists(details)).willReturn(details);
        assertThat(service.register(authDetails)).isEqualTo(details);
    }
}