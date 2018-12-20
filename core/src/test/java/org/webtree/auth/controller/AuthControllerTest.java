package org.webtree.auth.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.Token;
import org.webtree.auth.security.CombinedPasswordEncoder;
import org.webtree.auth.service.UserAuthenticationService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractControllerTest {
    private final static String USERNAME = "JOHN_SNOW";
    private static final String TOKEN = "NEKOT";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserAuthenticationService service;
    @MockBean
    private CombinedPasswordEncoder encoder;
    private AuthDetails authDetails;
    private Token token;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        authDetails = new AuthDetailsImpl(USERNAME,PASSWORD);
        token = () -> TOKEN;
    }

    @Test
    public void shouldReturn2XxOkIfUserDoesNotExist() throws Exception {
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isCreated());
        verify(service).register(authDetails);
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