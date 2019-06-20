package org.webtree.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.auth.domain.User;
import org.webtree.auth.exception.AuthenticationException;
import org.webtree.auth.time.TimeProvider;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    private static final String TEST_USERNAME = "testUser";
    private static final String USER_ID = "someUserId";
    private static final String PASSWORD = "someUserPassword";

    @Mock
    private TimeProvider timeProviderMock;

    private User user;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService.setExpiration(3600L);
        jwtTokenService.setSecret("mySecret");
        user = User.builder()
                .withId(USER_ID)
                .withUsername(TEST_USERNAME)
                .withPassword(PASSWORD)
                .withLastPasswordResetDate(new Date())
                .build();
    }

    @Test
    void testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() {
        when(timeProviderMock.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now());

        final String token = createToken();
        final String laterToken = createToken();

        assertThat(token).isNotEqualTo(laterToken);
    }

    @Test
    void getUsernameFromToken() {
        when(timeProviderMock.now()).thenReturn(DateUtil.now());

        final String token = createToken();

        assertThat(jwtTokenService.getUsernameFromToken(token)).isEqualTo(TEST_USERNAME);
    }

    @Test
    void shouldThrowExceptionIfTokenCantBeProcessed() {
        final String token = "123";
        assertThatThrownBy(() -> jwtTokenService.getUsernameFromToken(token))
                .isExactlyInstanceOf(AuthenticationException.class);
    }


    @Test
    void getCreatedDateFromToken() {
        final Date now = DateUtil.now();
        when(timeProviderMock.now()).thenReturn(now);

        final String token = createToken();

        assertThat(jwtTokenService.getIssuedAtDateFromToken(token)).isInSameMinuteWindowAs(now);
    }

    @Test
    void getExpirationDateFromToken() {
        final Date now = DateUtil.now();
        when(timeProviderMock.now()).thenReturn(now);
        final String token = createToken();

        final Date expirationDateFromToken = jwtTokenService.getExpirationDateFromToken(token);
        assertThat(DateUtil.timeDifference(expirationDateFromToken, now)).isCloseTo(3600000L, within(1000L));
    }

    @Test
    void expiredTokenCannotBeRefreshed() {
        when(timeProviderMock.now())
                .thenReturn(DateUtil.yesterday());
        String token = createToken();
        assertThatThrownBy(() -> jwtTokenService.canTokenBeRefreshed(token, DateUtil.tomorrow()))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void changedPasswordCannotBeRefreshed() {
        when(timeProviderMock.now())
                .thenReturn(DateUtil.now());
        String token = createToken();
        assertThat(jwtTokenService.canTokenBeRefreshed(token, DateUtil.tomorrow())).isFalse();
    }

    @Test
    void notExpiredCanBeRefreshed() {
        when(timeProviderMock.now())
                .thenReturn(DateUtil.now());
        String token = createToken();
        assertThat(jwtTokenService.canTokenBeRefreshed(token, DateUtil.yesterday())).isTrue();
    }

    @Test
    void canRefreshToken() {
        when(timeProviderMock.now())
                .thenReturn(DateUtil.now())
                .thenReturn(DateUtil.tomorrow());
        String firstToken = createToken();
        String refreshedToken = jwtTokenService.refreshToken(firstToken);
        Date firstTokenDate = jwtTokenService.getIssuedAtDateFromToken(firstToken);
        Date refreshedTokenDate = jwtTokenService.getIssuedAtDateFromToken(refreshedToken);
        assertThat(firstTokenDate).isBefore(refreshedTokenDate);
    }

    @Test
    void canValidateToken() {
        when(timeProviderMock.now()).thenReturn(DateUtil.now());

        String token = jwtTokenService.generateToken(User.builder().withUsername(TEST_USERNAME).build());
        assertThat(jwtTokenService.isTokenValid(token)).isTrue();
    }

    @Test
    void whenPassInvalidTokenShouldReturnFalse() {
        assertThat(jwtTokenService.isTokenValid("InvalidToken")).isFalse();
    }

    private String createToken() {
        return jwtTokenService.generateToken(user);
    }
}