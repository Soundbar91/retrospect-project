package com.soundbar91.retrospect_project.auth.service;

import com.soundbar91.retrospect_project.auth.controller.dto.RequestLoginUser;
import com.soundbar91.retrospect_project.user.entity.User;
import com.soundbar91.retrospect_project.global.exception.ApplicationException;
import com.soundbar91.retrospect_project.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soundbar91.retrospect_project.global.exception.errorCode.AuthErrorCode.NOT_MATCH_PASSWORD;
import static com.soundbar91.retrospect_project.global.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.global.exception.errorCode.UserErrorCode.WITHDREW_USER;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void loginUser(RequestLoginUser requestLoginUser, HttpServletRequest httpServletRequest) {
        User user = authenticateUser(requestLoginUser);
        authorizeSession(httpServletRequest, user);
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
    }

    private User authenticateUser(RequestLoginUser requestLoginUser) {
        User user = userRepository.findByUsername(requestLoginUser.username())
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (!user.isActive()) throw new ApplicationException(WITHDREW_USER);

        if (!passwordEncoder.matches(requestLoginUser.password(), user.getPassword()))
            throw new ApplicationException(NOT_MATCH_PASSWORD);

        return user;
    }

    private void authorizeSession(HttpServletRequest request, User user) {
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setMaxInactiveInterval(3600);
    }

}
