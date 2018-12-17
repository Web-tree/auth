package org.webtree.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * Created by Udjin Skobelev on 22.09.2018.
 */

@Component
public class CombinedPasswordEncoder implements PasswordEncoder {

    private PasswordEncoder encoder;
    private TextEncryptor encryptor;

    public CombinedPasswordEncoder(@Value(("${spring.security.encoder.password}")) String password,
                                   @Value(("${spring.security.encoder.salt}")) String salt) {
        encoder = new BCryptPasswordEncoder();
        encryptor = Encryptors.delux(password, salt);
    }

    public String encode(CharSequence charSequence) {
        String encodedPassword = encoder.encode(charSequence);
        return encryptor.encrypt(encodedPassword);
    }

    public boolean matches(CharSequence charSequence, String encodedPassword) {
        String decryptedPassword = encryptor.decrypt(encodedPassword);
        return encoder.matches(charSequence, decryptedPassword);
    }
}
