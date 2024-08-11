package com.soundbar91.retrospect_project.global.interceptor;

import com.soundbar91.retrospect_project.global.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.soundbar91.retrospect_project.global.exception.errorCode.UserErrorCode.NOT_LOGIN;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("userId") == null) throw new ApplicationException(NOT_LOGIN);
        else return true;
    }

}
