package org.webtree.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.webtree.auth.domain.view.Views;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class RestRepoConfig {

    @Value("${auth.repo.token}")
    private String token;

    @Bean
    public RestTemplate getRepoRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, getMessageConverter());
        restTemplate.setInterceptors(Collections.singletonList(new TokenHeaderInterceptor()));
        return restTemplate;
    }

    private HttpMessageConverter getMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithView(Views.Repo.class);
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    class TokenHeaderInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("x-api-key", token);
            return execution.execute(request, body);
        }
    }
}
