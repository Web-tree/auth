package org.webtree.auth.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.Token;
import org.webtree.auth.service.UserAuthenticationService;

public class SecurityControllerTest extends AbstractControllerTest {
    private static final String PASSWORD = "HAT";
    private static final String TEST_USERNAME = "username_test";
    private static final String TOKEN = "NEKOT";

    private AuthDetails authDetails;

    @MockBean
    private UserAuthenticationService service;

    private Token token;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        authDetails = AuthDetailsImpl.builder().username(TEST_USERNAME).password(PASSWORD).build();
        token = () -> TOKEN;
    }

    @Test
    public void whenLoginWithExistedUser_shouldReturnToken() throws Exception {
        given(service.login(authDetails)).willReturn(token);
        mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(TOKEN))
                .andReturn();
    }
}