package org.webtree.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.User;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.service.AuthenticationService;
import org.webtree.auth.time.TimeProvider;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private final static String USERNAME = "JOHN_SNOW";
    private static final String PASSWORD =
            "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2";
    private static final String ID = "someId";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private AuthRepository authRepository;
    @MockBean
    private TimeProvider timeProvider;
    private AuthDetails authDetails;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authDetails = new AuthDetails(USERNAME, PASSWORD);
        when(timeProvider.now()).thenReturn(new Date());
    }

    @Test
    void whenLoginWithExistedUser_shouldReturnValidToken() throws Exception {
        User user = User.builder().withId(ID).withUsername(USERNAME).withPassword(PASSWORD).build();
        when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token").value(new TokenMatcher()));
    }

    @Test
    void shouldReturnOkWhenTokenIsCorrect() throws Exception {
        User user = User.builder().withId(ID).withUsername(USERNAME).withPassword(PASSWORD).build();
        when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        Token token = authenticationService.login(authDetails);
        mockMvc.perform(
                post("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(token.getToken())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.id").value(ID));
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsNotCorrect() throws Exception {
        String invalidToken = "someToken";

        mockMvc.perform(
                post("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(invalidToken)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsExpired() throws Exception {
        User user = User.builder().withId(ID).withUsername(USERNAME).withPassword(PASSWORD).build();
        when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        reset(timeProvider);
        when(timeProvider.now()).thenReturn(new Date(0));
        String expiredToken = authenticationService.login(authDetails).getToken();
        reset(timeProvider);
        when(timeProvider.now()).thenReturn(new Date());

        mockMvc.perform(
                post("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(expiredToken)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(StringPatternMatcher.of("JWT expired at .*. Current time: .*, a difference of .* milliseconds.  Allowed clock skew: .* milliseconds.")));
    }

    @Nested
    class Registration {

        @Test
        void shouldReturnOkIfUserDoesNotExist() throws Exception {
            when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
            when(authRepository.save(any())).thenReturn(User.builder().withId(ID).withUsername(USERNAME).withPassword(PASSWORD).build());
            mockMvc
                    .perform(post("/rest/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authDetails)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(ID))
                    .andExpect(jsonPath("$.username").value(USERNAME))
                    .andExpect(jsonPath("$.password").doesNotExist())
            ;
        }

        @Test
        void shouldReturnBadRequestIfUserExist() throws Exception {
            when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.of(User.builder().build()));
            mockMvc
                    .perform(post("/rest/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authDetails)))
                    .andExpect(status().isBadRequest());
        }
    }
    static class StringPatternMatcher extends BaseMatcher<String> {

        private final String pattern;
        private final Pattern compiledPattern;

        private StringPatternMatcher(String pattern) {
            this.pattern = pattern;
            compiledPattern = Pattern.compile(pattern);
        }

        public static StringPatternMatcher of(String pattern) {
            return new StringPatternMatcher(pattern);
        }

        @Override public boolean matches(Object testingString) {
            return testingString instanceof String
                    && compiledPattern.matcher((String) testingString).matches();
        }

        @Override public void describeTo(Description description) {
            description.appendText("pattern - ");
            description.appendText(pattern);
        }
    }

    class TokenMatcher extends BaseMatcher<String> {
        @Override public boolean matches(Object o) {
            User user = authenticationService.decodeToken(o.toString());
            return ID.equals(user.getId())
                    && USERNAME.equals(user.getUsername());
        }

        @Override public void describeTo(Description description) {

        }
    }
}
