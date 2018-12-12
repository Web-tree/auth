package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WTUserDetails;

public abstract class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Override
    public WTUserDetails register(AuthDetails authDetails) {
        int passwordLength = authDetails.getPassword().length();

        if (passwordLength != 128) {
//            return ResponseEntity.badRequest().body("The password must be a representation of sha512");
            //todo throw
        }
        return null;
    }

    @Override
    public Token login(AuthDetails userDetails) throws UsernameNotFoundException {
        return null;
    }
}
