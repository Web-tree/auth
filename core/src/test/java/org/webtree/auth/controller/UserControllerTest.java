package org.webtree.auth.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.service.UserAuthenticationService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractControllerTest {
    private final static String USERNAME = "JOHN_SNOW";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserAuthenticationService service;
    private AuthDetails authDetails;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        authDetails = AuthDetailsImpl.builder().username(USERNAME).password(PASSWORD).build();
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
}