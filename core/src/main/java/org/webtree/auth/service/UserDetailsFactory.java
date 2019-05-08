package org.webtree.auth.service;

import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.User;

public abstract class UserDetailsFactory {
    abstract User createUserOf(AuthDetails authDetails);
}