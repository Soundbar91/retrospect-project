package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.dto.request.RequestLoginUser;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import com.soundbar91.retrospect_project.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseUser> loginUser(
            @Valid @RequestBody RequestLoginUser requestLoginUser,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        authService.loginUser(requestLoginUser, httpServletRequest);
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/main");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logoutUser(
            HttpServletRequest httpServletRequest
    ) {
        authService.logout(httpServletRequest);
        return ResponseEntity.noContent().build();
    }

}
