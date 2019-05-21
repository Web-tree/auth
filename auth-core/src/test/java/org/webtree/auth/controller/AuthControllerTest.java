package org.webtree.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.User;
import org.webtree.auth.exception.AuthenticationException;
import org.webtree.auth.security.CombinedPasswordEncoder;
import org.webtree.auth.service.AuthenticationService;
import org.webtree.auth.service.JwtTokenService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractControllerTest {
    private final static String USERNAME = "JOHN_SNOW";

    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private AuthenticationService service;
    private AuthDetails authDetails;
    @Captor
    private ArgumentCaptor<AuthDetails> authDetailsCaptor;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        authDetails = new AuthDetailsImpl(USERNAME, PASSWORD);
    }

    @Test
    void shouldReturnOkIfUserDoesNotExist(@Autowired CombinedPasswordEncoder encoder) throws Exception {
        doReturn(true).when(service).registerIfNotExists(authDetailsCaptor.capture());
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isCreated());
        AuthDetails authDetails = authDetailsCaptor.getValue();
        assertThat(authDetails.getUsername()).isEqualTo(USERNAME);
        assertThat(encoder.matches(PASSWORD, authDetails.getPassword())).isTrue();
    }

    @Test
    void shouldReturnBadRequestIfUserExist() throws Exception {
        User user = new User();
        user.setUsername(USERNAME);
        when(authRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.of(user));
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenLoginWithExistedUser_shouldReturnToken() throws Exception {
        User user = new User();
        user.setId("someId");
        user.setUsername(USERNAME);
        when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();
    }

    @Test
    void shouldReturnOkWhenTokenIsCorrect(@Autowired JwtTokenService tokenService) throws Exception {
//        String someToken = "someToken";
//        when(service.checkToken(someToken)).thenReturn(true);
        User user = new User();
        user.setId("123");
        user.setUsername(USERNAME);
        String token = tokenService.generateToken(user);
        when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        mockMvc.perform(
                get("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(token)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestIFTokenISNotCorrect() throws Exception {
        String someToken = "someToken";
        doReturn(false).when(service).checkToken(someToken);

        mockMvc.perform(
                get("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(someToken)
        ).andExpect(status().is(401));
    }

    //simple controller advice test
    @Test
    void shouldReturnBadRequestIfExceptionOccur() throws Exception {
        String someToken = "someToken";
        String someErrorMSG = "someErrorMSG";
        doThrow(new AuthenticationException(someErrorMSG)).when(service).checkToken(someToken);

        mockMvc.perform(
                get("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(someToken)
        ).andExpect(status().isBadRequest())
                .andExpect(content().string(someErrorMSG));
    }
}