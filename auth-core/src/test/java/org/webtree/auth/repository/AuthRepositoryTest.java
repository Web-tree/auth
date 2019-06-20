package org.webtree.auth.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpServerErrorException;
import org.webtree.auth.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class AuthRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateTest.class);

    private static final String USERNAME = "someUsername";
    private static final String PASSWORD = "somePassword";
    @Autowired
    private AuthRepository repo;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${auth.repo.url}")
    private String repoUrl;

    private static ClientAndServer clientAndServer;

    @BeforeAll
    static void beforeAll() {
        clientAndServer = ClientAndServer.startClientAndServer(32323);
    }

    @AfterEach
    void tearDown() {
        clientAndServer.reset();
    }

    @AfterAll
    static void afterAll() {
        clientAndServer.stop(true);
    }

    @Nested
    class Save {
        @Test
        void shouldSendUserToUrlFromConfig() throws Exception {
            User userForSave = buildStandardUser();
            clientAndServer.when(new HttpRequest()).respond(new HttpResponse());
            repo.save(userForSave);
            HttpRequest[] httpRequests = clientAndServer.retrieveRecordedRequests(new HttpRequest());
            assertThat(httpRequests).hasSize(1);
            HttpRequest request = httpRequests[0];
            User user = objectMapper.readValue(request.getBody().toString(), User.class);
            assertThat(user).isEqualTo(userForSave);
            assertThat(request.getPath().getValue()).isEqualTo("/user");
            assertThat(request.getMethod().getValue()).isEqualTo("POST");
        }

        @Test
        void shouldReturnUserFromResponse() throws Exception {
            User user = User.builder().withUsername(USERNAME).withPassword(PASSWORD).withId("someId").build();
            HttpResponse httpResponse = new HttpResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(user));
            clientAndServer.when(new HttpRequest()).respond(httpResponse);

            User savedUser = repo.save(buildStandardUser());
            assertThat(savedUser).isEqualTo(user);
        }
    }

    @Nested
    class Find {
        @Test
        void shouldCallFindUrl() {
            HttpRequest expectedRequest = new HttpRequest()
                    .withMethod("GET")
                    .withPath("/user/name/" + USERNAME);
            clientAndServer.when(expectedRequest).respond(new HttpResponse());

            repo.findByUsername(USERNAME);

            HttpRequest[] httpRequests = clientAndServer.retrieveRecordedRequests(new HttpRequest());
            assertThat(httpRequests).hasSize(1);
        }

        @Test
        void shouldReturnUserFromRequest() throws Exception {
            User user = buildStandardUser();
            HttpResponse httpResponse = new HttpResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(user));
            clientAndServer.when(new HttpRequest()).respond(httpResponse);

            Optional<User> userOptional = repo.findByUsername(USERNAME);
            assertThat(userOptional).isPresent();
            assertThat(userOptional.get()).isEqualTo(user);
        }

        @Test
        void shouldReturnEmptyOptionalWhenRepoResponseIs404() {
            clientAndServer.when(new HttpRequest()).respond(new HttpResponse().withStatusCode(404));

            Optional<User> userOptional = repo.findByUsername(USERNAME);

            assertThat(userOptional).isNotPresent();
        }

        @Test
        void shouldRethrowNotHandlingExceptions() {
            clientAndServer.when(new HttpRequest()).respond(new HttpResponse().withStatusCode(500));
            assertThatThrownBy(() -> repo.findByUsername(USERNAME)).isInstanceOf(HttpServerErrorException.class);
        }
    }

    private User buildStandardUser() {
        return User.builder().withUsername(USERNAME).withPassword(PASSWORD).build();
    }
}