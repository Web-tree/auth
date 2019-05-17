package org.webtree.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.Token;
import org.webtree.auth.exception.AuthenticationException;
import org.webtree.auth.security.CombinedPasswordEncoder;
import org.webtree.auth.service.AuthenticationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest extends AbstractControllerTest {
    private final static String USERNAME = "JOHN_SNOW";
    private static final String TOKEN = "NEKOT";
    private static final String ENCODED_PASSWORD = "PASSWORD_THAT_ENCODED";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService service;
    @MockBean
    private CombinedPasswordEncoder encoder;
    private AuthDetails authDetails;
    private AuthDetails authDetailsWithEncodedPassword;
    private Token token;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        given(encoder.encode(PASSWORD)).willReturn(ENCODED_PASSWORD);
        authDetails = new AuthDetailsImpl(USERNAME, PASSWORD);
        authDetailsWithEncodedPassword = new AuthDetailsImpl(USERNAME, encoder.encode(PASSWORD));
        token = () -> TOKEN;
    }

    @Test
    void shouldReturnOkIfUserDoesNotExist() throws Exception {
        when(service.registerIfNotExists(any(AuthDetails.class))).thenReturn(true);
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isCreated());
        verify(service).registerIfNotExists(authDetailsWithEncodedPassword);
    }

    @Test
    void shouldReturnBadRequestIfUserExist() throws Exception {
        when(service.registerIfNotExists(any(AuthDetails.class))).thenReturn(false);

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isBadRequest());
        verify(service).registerIfNotExists(authDetailsWithEncodedPassword);
    }

    @Test
    void whenLoginWithExistedUser_shouldReturnToken() throws Exception {
        given(service.login(authDetailsWithEncodedPassword)).willReturn(token);
        mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(TOKEN))
                .andReturn();
    }

    @Test
    void shouldReturnOkWhenTokenIsCorrect() throws Exception {
        String someToken = "someToken";
        when(service.checkToken(someToken)).thenReturn(true);

        mockMvc.perform(
                get("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(someToken)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestIFTokenISNotCorrect() throws Exception {
        String someToken = "someToken";
        when(service.checkToken(someToken)).thenReturn(false);

        mockMvc.perform(
                get("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(someToken)
        ).andExpect(status().is(401));
    }

    //simple controller advice test
    @Test
    void shouldReturnBadRequestIfExceptionOccur() throws Exception {
        String someToken = "someToken";
        String someErrorMSG = "someErrorMSG";
        when(service.checkToken(someToken)).thenThrow(new AuthenticationException(someErrorMSG));

        mockMvc.perform(
                get("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(someToken)
        ).andExpect(status().isBadRequest())
                .andExpect(content().string(someErrorMSG));
    }
}