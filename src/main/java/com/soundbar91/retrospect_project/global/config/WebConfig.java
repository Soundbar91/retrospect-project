package com.soundbar91.retrospect_project.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.soundbar91.retrospect_project.global.interceptor.AuthInterceptor;
import com.soundbar91.retrospect_project.global.interceptor.CommentInterceptor;
import com.soundbar91.retrospect_project.global.interceptor.PostInterceptor;
import com.soundbar91.retrospect_project.global.interceptor.ProblemInterceptor;
import com.soundbar91.retrospect_project.global.interceptor.ResultInterceptor;
import com.soundbar91.retrospect_project.global.interceptor.UserInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor certInterceptor;
    private final UserInterceptor userInterceptor;
    private final ProblemInterceptor problemInterceptor;
    private final ResultInterceptor resultInterceptor;
    private final PostInterceptor boardInterceptor;
    private final CommentInterceptor commentInterceptor;
    private final CorsProperties corsProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(certInterceptor)
            .order(1)
            .addPathPatterns("/auth/logout");

        registry.addInterceptor(userInterceptor)
            .order(1)
            .addPathPatterns("/user/**");

        registry.addInterceptor(problemInterceptor)
            .order(1)
            .addPathPatterns("/problem/**", "/problems");

        registry.addInterceptor(resultInterceptor)
            .order(1)
            .addPathPatterns("/result/**", "/results");

        registry.addInterceptor(boardInterceptor)
            .order(1)
            .addPathPatterns("/post/**", "/posts");

        registry.addInterceptor(commentInterceptor)
            .order(1)
            .addPathPatterns("/comment/**");

    }

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //         .allowedOrigins(corsProperties.getAllowedOrigins().toArray(new String[0]))
    //         .allowedHeaders("GET", "POST", "PUT", "DELETE")
    //         .allowedHeaders("*")
    //         .allowCredentials(true)
    //         .maxAge(3600);
    // }
}
