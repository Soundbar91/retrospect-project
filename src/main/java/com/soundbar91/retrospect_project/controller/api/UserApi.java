package com.soundbar91.retrospect_project.controller.api;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestPasswordChange;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API")
public interface UserApi {

    @Operation(summary = "회원 가입")
    @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/user")
    ResponseEntity<ResponseUser> createUser(
            @Valid @RequestBody RequestCreateUser requestCreateUser
    );

    @Operation(summary = "비밀번호 변경")
    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(mediaType = "application/json"))
    @PutMapping("/user/password")
    ResponseEntity<Void> changePassword(
            @Valid @RequestBody RequestPasswordChange requestPasswordChange,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "유저 조회")
    @ApiResponse(responseCode = "200", description = "유저 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/user/{username}")
    ResponseEntity<ResponseUser> getUser(
            @PathVariable(value = "username") String username
    );

    @Operation(summary = "회원 탈퇴")
    @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/user")
    ResponseEntity<Void> deleteUser(
            HttpServletRequest httpServletRequest
    );

}
