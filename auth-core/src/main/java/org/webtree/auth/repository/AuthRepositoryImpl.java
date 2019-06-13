package org.webtree.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.webtree.auth.domain.User;

import java.util.Optional;

@Repository
public class AuthRepositoryImpl implements AuthRepository {
    private final UserRepository userRepository;

    @Autowired
    public AuthRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}