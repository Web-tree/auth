package org.webtree.auth.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RestTemplateTest {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.repo.token}")
    private String token;

    @Test
    void tokenNotEmpty() {
        assertThat(token).isNotBlank();
    }

    @Test
    void shouldAddTokenHeader() {
        ClientAndServer clientAndServer = startClientAndServer();
        clientAndServer.when(new HttpRequest()).respond(new HttpResponse());
        String url = "http://localhost:" + clientAndServer.getPort();
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("test"), String.class);
        HttpRequest[] httpRequests = clientAndServer.retrieveRecordedRequests(new HttpRequest());
        assertThat(httpRequests).hasSize(1);
        assertThat(httpRequests[0].getHeaders()).contains(new Header("x-api-key", token));
    }

    private ClientAndServer startClientAndServer() {
        int i = 0;
        while (i < 1000) {
            int port = ThreadLocalRandom.current().nextInt(30000, 35000);
            try {
                return ClientAndServer.startClientAndServer(port);
            } catch (Exception e) {
                i++;
                log.debug("Port {} already bind", port, e);
            }
        }
        throw new RuntimeException("Can't start mock server.");
    }
}
