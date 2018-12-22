package org.webtree.auth.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = AuthDetailsImpl.class)
public interface AuthDetails {
    String getUsername();

    String getPassword();
}