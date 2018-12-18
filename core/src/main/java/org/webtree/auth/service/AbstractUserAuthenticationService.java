package org.webtree.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.WTUserDetails;
import org.webtree.auth.repository.AuthRepository;

import java.util.Optional;

public abstract class AbstractUserAuthenticationService implements UserAuthenticationService {
    protected abstract AuthRepository getRepository();

    @Override
    public WTUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<WTUserDetails> details =
                getRepository().findByUsername(s);
        if (!details.isPresent()) throw new UsernameNotFoundException(s);
        return details.get();
    }
}