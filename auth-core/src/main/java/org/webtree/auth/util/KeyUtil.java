package org.webtree.auth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyUtil.class);
    private static final String SPACES_REGEX = "\\s*";
    private static final String COMMENTS_REGEX = "-----[A-Z]+-----";

    public static PublicKey getPublicKey(String publicKey, String algorithm) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeKey(publicKey));
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            LOGGER.error("Could not reconstruct the public key");
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey getPrivateKey(String privateKey, String algorithm) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeKey(privateKey));
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            LOGGER.error("Could not reconstruct the public key");
            throw new RuntimeException(e);
        }
    }

    private static byte[] decodeKey(String key) {
        return Base64.getDecoder().decode(
                key.replaceAll(SPACES_REGEX, "").replaceAll(COMMENTS_REGEX, "")
        );
    }
}
