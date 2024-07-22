package com.soundbar91.retrospect_project.config;

import com.soundbar91.retrospect_project.interceptor.CertInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CertInterceptor certInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(certInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/join", "/login");
    }

}
