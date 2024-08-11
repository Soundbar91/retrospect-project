package com.soundbar91.retrospect_project.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "좋아요 API")
public interface LikeApi {

    @Operation(summary = "게시글 좋아요")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/post/{postId}/like")
    ResponseEntity<Void> likePost(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "게시글 좋아요 개수 조회")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 개수 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/post/{postId}/likes")
    ResponseEntity<Integer> getLikePostCount(
            @PathVariable(name = "postId") Long postId
    );

    @Operation(summary = "게시글 좋아요 취소")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 취소 성공", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/post/{postId}/like")
    ResponseEntity<Void> likePostCancel(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "댓글 좋아요")
    @ApiResponse(responseCode = "200", description = "댓글 좋아요 성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/comment/{commentId}/like")
    ResponseEntity<Void> likeComment(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "댓글 좋아요 개수 조회")
    @ApiResponse(responseCode = "200", description = "댓글 좋아요 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("comment/{commentId}/likees")
    ResponseEntity<Integer> getLikeCommentCount(
            @PathVariable(name = "commentId") Long commentId
    );

    @Operation(summary = "댓글 좋아요 취소")
    @ApiResponse(responseCode = "200", description = "댓글 좋아요 취소 성공", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/comment/{commentId}/like")
    ResponseEntity<Void> likeCommentCancel(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    );

}
