package org.webtree.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webtree.auth.security.CombinedPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class CombinedPasswordEncoderTest {
    private final static String PASSWORD_TO_ENCODE = "usualPassword";
    private PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        encoder = new CombinedPasswordEncoder("longPassword", "9e0b5328c644e94a");
    }

    @Test
    public void shouldEncodePassword() {
        assertThat(encoder.encode(PASSWORD_TO_ENCODE)).isNotEmpty();
    }

    @Test
    public void shouldReturnTrueIfPasswordMatches() {
        String encodedString = encoder.encode(PASSWORD_TO_ENCODE);
        assertThat(encoder.matches(PASSWORD_TO_ENCODE, encodedString)).isTrue();
    }

    @Test
    public void shouldReturnFalseIfPasswordDoNotMatches() {
        String encodedString = encoder.encode(PASSWORD_TO_ENCODE);
        assertThat(encoder.matches("newPassword", encodedString)).isFalse();
    }
}