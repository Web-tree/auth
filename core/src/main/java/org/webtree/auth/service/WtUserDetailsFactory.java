package org.webtree.auth.service;

import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.WtUserDetails;

public abstract class WtUserDetailsFactory {
    abstract WtUserDetails createUserOf(AuthDetails authDetails);
}