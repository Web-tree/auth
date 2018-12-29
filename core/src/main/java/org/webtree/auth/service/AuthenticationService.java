package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WtUserDetails;

public interface AuthenticationService extends UserDetailsService {
    WtUserDetails register(AuthDetails userDetails);

    Token login(AuthDetails userDetails);

    @Override
    WtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
