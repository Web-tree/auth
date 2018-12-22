package org.webtree.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;

@Component
public class RequestBodyParamArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private PasswordEncoder encoder;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestBodyParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        String json = ((ServletWebRequest) nativeWebRequest).getRequest().getReader().readLine();


        if (!modelAndViewContainer.getModel().containsAttribute("json")) {
            HashMap<String,String> result =
                    new ObjectMapper().readValue(json, HashMap.class);

           if (methodParameter.getParameterAnnotation(RequestBodyParam.class).encode()){
               result.computeIfPresent("password",(x,y)-> encoder.encode(y));
           }

            modelAndViewContainer.addAttribute("json", result);

            result.forEach((key,value)-> modelAndViewContainer.addAttribute(key,value));
        }
        Object o = modelAndViewContainer.getModel().get(methodParameter.getParameterName());
        return o;
    }
}