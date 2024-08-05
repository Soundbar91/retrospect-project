package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.service.LikesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PutMapping("/post/{postId}/like")
    public ResponseEntity<Void> likePost(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        likesService.likePost(postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}/like")
    public ResponseEntity<Integer> getLikesCount(
            @PathVariable(name = "postId") Long postId
    ) {
        int likesCount = likesService.getLikesCount(postId);
        return ResponseEntity.ok(likesCount);
    }

    @DeleteMapping("/post/{postId}/like")
    public ResponseEntity<Void> likeCancel(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        likesService.likeCancel(postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

}
