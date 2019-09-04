package org.webtree.auth.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Pkcs12Keys {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pkcs12Keys.class);

    private static final String PKCS_12 = "PKCS12";

    public static KeyPair keyPairForFile(String pkcs12FilePath, String password, String alias) {
        final File file = new File(pkcs12FilePath);
        try (InputStream inputStream = new FileInputStream(file)) {
            KeyStore keyStore = KeyStore.getInstance(PKCS_12);
            keyStore.load(inputStream, password.toCharArray());
            Certificate certificate = keyStore.getCertificate(alias);
            PublicKey publicKey = certificate.getPublicKey();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            LOGGER.error("The system has failed to generate a key pair: {}", e.getMessage());
            return null;
        }
    }
}
