package org.webtree.auth.domain;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface WTUserDetails<ID> extends UserDetails {
    ID getId();
    Date getLastPasswordResetDate();
}