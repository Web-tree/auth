package org.webtree.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.webtree.auth.domain.AuthDetails;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


@Component
public class AuthDetailsRequestBodyProcessor extends RequestResponseBodyMethodProcessor {
    public AuthDetailsRequestBodyProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Value("#{AuthPropertiesBean.details}")
    private Class<? extends AuthDetails> detailsImplClass;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthDetails.class);
    }

    @Override
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
                                                   MethodParameter parameter, Type paramType)
            throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        return super.readWithMessageConverters(webRequest, parameter, detailsImplClass);
    }
}