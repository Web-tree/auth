package org.webtree.auth.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.auth.security.CombinedPasswordEncoder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EncodePasswordDeserializerTest {
    private final static String STRING_BEFORE_ENCODING =
            "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2";

    private final static String STRING_AFTER_ENCODING = "yYy";
    @InjectMocks
    private EncodePasswordDeserializer deserializer;
    private ObjectMapper mapper;
    @Mock
    private JsonParser parser;
    @Mock
    private CombinedPasswordEncoder encoder;
    @Mock
    private DeserializationContext ctxt;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsWrongLength() throws IOException {
        String passwordWithWrongLength = "pwd";
        given(parser.getText()).willReturn(passwordWithWrongLength);
        assertThatThrownBy(() -> deserializer.deserialize(parser, ctxt)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldEncodeGivenString() throws IOException {
        given(encoder.encode(STRING_BEFORE_ENCODING)).willReturn(STRING_AFTER_ENCODING);
        given(parser.getText()).willReturn(STRING_BEFORE_ENCODING);
        assertThat(deserializer.deserialize(parser, ctxt)).isEqualTo(STRING_AFTER_ENCODING);
    }
}