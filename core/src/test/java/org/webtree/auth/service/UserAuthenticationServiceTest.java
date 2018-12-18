package org.webtree.auth.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webtree.auth.domain.AuthDetails;
import org.webtree.auth.domain.Token;
import org.webtree.auth.domain.WTUserDetails;
import org.webtree.auth.repository.AuthRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthenticationServiceTest {
    private static final String TEST_USERNAME = "Johnny";

    @Mock
    private AuthRepository repository;
    @Mock
    private WTUserDetails details;

    private AbstractUserAuthenticationService service;

    @Before
    public void setUp() throws Exception {
        service = createService();
    }

    @Test
    public void shouldReturnUserByUsername() {
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(details));
        assertThat(service.loadUserByUsername(TEST_USERNAME)).isEqualTo(details);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionIfUserWasNotFound() {
        given(repository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        service.loadUserByUsername(TEST_USERNAME);
    }

    private AbstractUserAuthenticationService createService() {
        return new AbstractUserAuthenticationService() {
            @Override
            protected AuthRepository getRepository() {
                return repository;
            }

            @Override
            public WTUserDetails register(AuthDetails userDetails) {
                return null;
            }

            @Override
            public Token login(AuthDetails userDetails) throws UsernameNotFoundException {
                return null;
            }
        };
    }
}