package org.webtree.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.annotations.WithDatabase;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.service.AuthenticationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithDatabase
@AutoConfigureMockMvc
class AuthControllerTest {
    private final static String USERNAME = "JOHN_SNOW";
    private static final String PASSWORD =
            "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationService service;
    private AuthDetails authDetails;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authDetails = new AuthDetails(USERNAME, PASSWORD);
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnOkIfUserDoesNotExist() throws Exception {
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.password").doesNotExist())
        ;
        assertThat(service.login(authDetails)).isNotNull();
    }

    @Test
    void shouldReturnBadRequestIfUserExist() throws Exception {
        service.register(authDetails);
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenLoginWithExistedUser_shouldReturnToken() throws Exception {
        service.register(authDetails);

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
    void shouldReturnOkWhenTokenIsCorrect() throws Exception {
        service.register(authDetails);
        Token token = service.login(authDetails);
        mockMvc.perform(
                post("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(token.getToken())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME));
    }

    @Test
    void shouldReturnBadRequestIFTokenISNotCorrect() throws Exception {
        String invalidToken = "someToken";

        mockMvc.perform(
                get("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(invalidToken)
        ).andExpect(status().isUnauthorized());
    }
}
