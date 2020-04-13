package org.webtree.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.webtree.auth.domain.User;

import java.util.Optional;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final RestTemplate restTemplate;

    @Value("${auth.repo.url}")
    private String repoUrl;

    @Autowired
    public AuthRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String url = repoUrl + "/user/name/" + username;
        try{
            ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, User.class);
            return Optional.ofNullable(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public User save(User user) {
        ResponseEntity<User> responseEntity = restTemplate.exchange(repoUrl + "/user", HttpMethod.POST, new HttpEntity<>(user), User.class);
        return responseEntity.getBody();
    }
}