package com.soundbar91.retrospect_project.user.controller;

import com.soundbar91.retrospect_project.user.controller.dto.RequestCreateUser;
import com.soundbar91.retrospect_project.user.controller.dto.RequestPasswordChange;
import com.soundbar91.retrospect_project.user.controller.dto.ResponseUser;
import com.soundbar91.retrospect_project.auth.service.AuthService;
import com.soundbar91.retrospect_project.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

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
