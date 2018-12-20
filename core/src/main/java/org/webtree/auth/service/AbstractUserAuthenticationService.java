package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.WtUserDetails;
import org.webtree.auth.repository.AuthRepository;

public abstract class AbstractUserAuthenticationService implements UserAuthenticationService {
    protected abstract AuthRepository getRepository();

    @Override
    public WtUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getRepository().findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }
}