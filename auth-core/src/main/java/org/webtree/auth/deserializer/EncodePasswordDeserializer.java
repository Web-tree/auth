package org.webtree.auth.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EncodePasswordDeserializer extends JsonDeserializer<String> {
    private PasswordEncoder encoder;

    @Autowired
    public EncodePasswordDeserializer(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String original = jsonParser.getText();
        if (check(original))
            throw new IllegalArgumentException("Password's length should be a representation of sha512");
        return encoder.encode(original);
    }

    private boolean check(String password) {
        return password.length() != 128;
    }
}