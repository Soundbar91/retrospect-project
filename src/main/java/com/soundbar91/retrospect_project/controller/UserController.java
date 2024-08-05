package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.service.AuthService;
import com.soundbar91.retrospect_project.service.UserService;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestLoginUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestPasswordChange;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/user")
    public ResponseEntity<ResponseUser> createUser(
            @Valid @RequestBody RequestCreateUser requestCreateUser
    ) {
        userService.createUser(requestCreateUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody RequestPasswordChange requestPasswordChange,
            HttpServletRequest httpServletRequest
    ) {
        userService.changePassword(requestPasswordChange, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ResponseUser> getUser(
            @PathVariable(value = "username") String username
    ) {
        ResponseUser user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(
            HttpServletRequest httpServletRequest
    ) {
        userService.deleteUser(httpServletRequest);
        authService.logout(httpServletRequest);
        return ResponseEntity.noContent().build();
    }

}
