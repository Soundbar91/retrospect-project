package com.soundbar91.retrospect_project.global.config;

import com.soundbar91.retrospect_project.global.interceptor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor certInterceptor;
    private final UserInterceptor userInterceptor;
    private final ProblemInterceptor problemInterceptor;
    private final ResultInterceptor resultInterceptor;
    private final PostInterceptor boardInterceptor;
    private final CommentInterceptor commentInterceptor;

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

}
