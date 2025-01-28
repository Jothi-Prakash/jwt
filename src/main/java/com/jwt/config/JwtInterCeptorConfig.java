package com.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jwt.DTO.RequestMeta;
import com.jwt.interceptor.JwtInterceptor;

@Component
public class JwtInterCeptorConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Autowired
    public JwtInterCeptorConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/user/**") 
                .excludePathPatterns("/user/login", "/user/signup", "/user/refresh");
    }

    @Bean
    @RequestScope
    public RequestMeta requestMeta() {
        return new RequestMeta();
    }
}
