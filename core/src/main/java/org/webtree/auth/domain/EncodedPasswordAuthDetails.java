package org.webtree.auth.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public class EncodedPasswordAuthDetails implements AuthDetails {

    private AuthDetails details;
    private String encodedPassword;
    private PasswordEncoder encoder;

    public EncodedPasswordAuthDetails(AuthDetails details, PasswordEncoder encoder) {
        this.details = details;
        this.encoder = encoder;
    }

    @Override
    public String getUsername() {
        return details.getPassword();
    }

    @Override
    public String getPassword() {
        if (encodedPassword != null) return encodedPassword;
        encodedPassword = encoder.encode(details.getPassword());
        return encodedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncodedPasswordAuthDetails that = (EncodedPasswordAuthDetails) o;
        return Objects.equals(details, that.details) &&
                Objects.equals(encodedPassword, that.encodedPassword) &&
                Objects.equals(encoder, that.encoder);
    }

    @Override
    public int hashCode() {

        return Objects.hash(details, encodedPassword, encoder);
    }
}
