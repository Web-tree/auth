package org.webtree.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.AuthDetailsImpl;
import org.webtree.auth.domain.EncodedPasswordAuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.security.CombinedPasswordEncoder;
import org.webtree.auth.service.AuthenticationService;

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
    private AuthenticationService service;
    @Autowired
    private CombinedPasswordEncoder encoder;
    private AuthDetails authDetails;
    private Token token;
    private EncodedPasswordAuthDetails encodedPasswordAuthDetails;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        authDetails = new AuthDetailsImpl(USERNAME, PASSWORD);
        encodedPasswordAuthDetails = new EncodedPasswordAuthDetails(authDetails, encoder);
        token = () -> TOKEN;
    }
//
//    @Test
//    public void shouldReturn2XxOkIfUserDoesNotExist() throws Exception {
//        mockMvc
//                .perform(post("/rest/user/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authDetails)))
//                .andExpect(status().isCreated());
//        verify(service).register(encodedPasswordAuthDetails);
//    }
//
//    @Test
//    public void whenLoginWithExistedUser_shouldReturnToken() throws Exception {
//        given(service.login(encodedPasswordAuthDetails)).willReturn(token);
//        mockMvc.perform(
//                post("/rest/token/new")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authDetails))
//        )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value(TOKEN))
//                .andReturn();
//    }

    @Test
    public void whenLoginWithExistedUser_shouldReturnToken1() throws Exception {
        given(service.login(encodedPasswordAuthDetails)).willReturn(token);
        mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("password=password")
                        .content(objectMapper.writeValueAsString(authDetails))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(TOKEN))
                .andReturn();
    }
}