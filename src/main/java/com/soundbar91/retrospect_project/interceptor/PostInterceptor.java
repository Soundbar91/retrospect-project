package com.soundbar91.retrospect_project.interceptor;

import com.soundbar91.retrospect_project.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_LOGIN;

@Component
public class PostInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("userId") == null) throw new ApplicationException(NOT_LOGIN);
        else return true;
    }

}
