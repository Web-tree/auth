package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WTUserDetails;

public interface UserAuthenticationService {
    WTUserDetails register(AuthDetails userDetails);

    Token login(AuthDetails userDetails) throws UsernameNotFoundException;
}
