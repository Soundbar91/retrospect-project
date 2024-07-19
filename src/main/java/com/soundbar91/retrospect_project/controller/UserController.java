package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.Service.UserService;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestLoginUser;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ResponseUser> createUser(
            @Valid @RequestBody RequestCreateUser requestCreateUser
    ) {
        ResponseUser user = userService.createUser(requestCreateUser);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseUser> loginUser(
            @Valid @RequestBody RequestLoginUser requestLoginUser,
            HttpServletRequest httpServletRequest
    ) {
        ResponseUser user = userService.loginUser(requestLoginUser, httpServletRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(
            HttpServletRequest httpServletRequest
    ) {
        userService.logout(httpServletRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/withdrawal/{id}")
    public ResponseEntity<Void> withdrawal(
            @PathVariable("id") Long id,
            HttpServletRequest httpServletRequest
    ) {
        userService.withdrawalUser(id);
        userService.logout(httpServletRequest);
        return ResponseEntity.noContent().build();
    }
}
