/*
 * $
 *
 * Copyright (c) 2019  Pegasystems Inc.
 * All rights reserved.
 *
 * This  software  has  been  provided pursuant  to  a  License
 * Agreement  containing  restrictions on  its  use.   The  software
 * contains  valuable  trade secrets and proprietary information  of
 * Pegasystems Inc and is protected by  federal   copyright law.  It
 * may  not be copied,  modified,  translated or distributed in  any
 * form or medium,  disclosed to third parties or used in any manner
 * not provided for in  said  License Agreement except with  written
 * authorization from Pegasystems Inc.
 */
package org.webtree.auth.config;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.webtree.auth.util.KeyUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class AsymmetricEncryptionConfig {
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.RS256;

    @Value("#{AuthPropertiesBean.jwt.privateKey}")
    private String privateKey;
    @Value("#{AuthPropertiesBean.jwt.publicKey}")
    private String publicKey;

    @Bean
    public KeyPair keyPair() {
        PublicKey publicKey = KeyUtil.getPublicKey(this.publicKey, ALGORITHM.getFamilyName());
        PrivateKey privateKey = KeyUtil.getPrivateKey(this.privateKey, ALGORITHM.getFamilyName());
        return new KeyPair(publicKey, privateKey);
    }

    @Bean
    public SignatureAlgorithm signatureAlgorithm() {
        return ALGORITHM;
    }
}
