package org.webtree.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
    private AuthenticationService service;
    @MockBean
    private AuthRepository authRepository;
    private AuthDetails authDetails;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authDetails = new AuthDetails(USERNAME, PASSWORD);
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

    @Test
    void whenLoginWithExistedUser_shouldReturnToken() throws Exception {
        User user = User.builder().withId(ID).withUsername(USERNAME).withPassword(PASSWORD).build();
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
    void shouldReturnOkWhenTokenIsCorrect() throws Exception {
        User user = User.builder().withId(ID).withUsername(USERNAME).withPassword(PASSWORD).build();
        when(authRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        Token token = service.login(authDetails);
        mockMvc.perform(
                post("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(token.getToken())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.id").value(ID));
    }

    @Test
    void shouldReturnBadRequestIFTokenISNotCorrect() throws Exception {
        String invalidToken = "someToken";

        mockMvc.perform(
                post("/rest/checkToken").contentType(MediaType.APPLICATION_JSON).content(invalidToken)
        ).andExpect(status().isUnauthorized());
    }
}
