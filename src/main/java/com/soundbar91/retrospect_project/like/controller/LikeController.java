package com.soundbar91.retrospect_project.like.controller;

import com.soundbar91.retrospect_project.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController implements LikeApi {

    private final LikeService likesService;

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Void> likePost(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        likesService.likePost(postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}/likes")
    public ResponseEntity<Integer> getLikePostCount(
            @PathVariable(name = "postId") Long postId
    ) {
        int likesCount = likesService.getLikePostCount(postId);
        return ResponseEntity.ok(likesCount);
    }

    @DeleteMapping("/post/{postId}/like")
    public ResponseEntity<Void> likePostCancel(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        likesService.likePostCancel(postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<Void> likeComment(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        likesService.likeComment(commentId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("comment/{commentId}/likees")
    public ResponseEntity<Integer> getLikeCommentCount(
            @PathVariable(name = "commentId") Long commentId
    ) {
        int likesCount = likesService.getLikeCommentCount(commentId);
        return ResponseEntity.ok(likesCount);
    }

    @DeleteMapping("/comment/{commentId}/like")
    public ResponseEntity<Void> likeCommentCancel(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        likesService.likeCommentCancel(commentId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

}
