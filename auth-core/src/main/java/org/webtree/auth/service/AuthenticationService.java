package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.User;

public interface AuthenticationService extends UserDetailsService {
    boolean registerIfNotExists(AuthDetails userDetails);

    Token login(AuthDetails userDetails);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    String checkToken(String someToken);
}
