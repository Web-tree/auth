package org.webtree.auth.controller;

import com.datastax.driver.core.Cluster;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.auth.AbstractSpringTest;
import org.webtree.auth.config.CassandraAuthConfiguration;
import org.webtree.auth.repository.AuthRepository;
import org.webtree.auth.repository.UserLockRepository;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractSpringTest {
    static final String PASSWORD =
            "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2";

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthRepository authRepository;

    @MockBean
    protected UserLockRepository lockRepository;

    @MockBean
    private CassandraAuthConfiguration configuration;

    @MockBean
    private Cluster cluster;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }
}