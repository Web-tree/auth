package org.webtree.auth.service;

import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.User;

public interface AuthenticationService {

    Token login(AuthDetails userDetails);

    User decodeToken(String someToken);

    User register(AuthDetails authDetails) throws UserAlreadyRegistered;

    class UserAlreadyRegistered extends RuntimeException{}
}
