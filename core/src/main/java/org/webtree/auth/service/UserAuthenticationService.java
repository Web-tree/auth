package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WtUserDetails;

public interface UserAuthenticationService extends UserDetailsService {
    WtUserDetails register(AuthDetails userDetails);

    Token login(AuthDetails userDetails) throws UsernameNotFoundException;

    @Override
    WtUserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}
