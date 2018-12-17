package org.webtree.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.AbstractSpringTest;


@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractSpringTest {

    @Autowired
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }
}