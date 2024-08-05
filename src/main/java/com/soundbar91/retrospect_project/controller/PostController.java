package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreatePost;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdatePost;
import com.soundbar91.retrospect_project.controller.dto.response.ResponsePost;
import com.soundbar91.retrospect_project.entity.keyInstance.Category;
import com.soundbar91.retrospect_project.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/problem/{problemId}/post")
    public ResponseEntity<Void> createPost(
            @Valid @RequestBody RequestCreatePost requestCreatePost,
            @PathVariable(value = "problemId") Long problemId,
            HttpServletRequest httpServletRequest
    ) {
        postService.createPost(requestCreatePost, problemId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<ResponsePost>> getPosts(
            @RequestParam(value = "problemId", required = false) Long problemId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "category", required = false) Category category
    ) {
        List<ResponsePost> postByParam = postService.getPosts(problemId, username, category);
        return ResponseEntity.ok(postByParam);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponsePost> getPost(
            @PathVariable(value = "postId") Long postId
    ) {
        ResponsePost post = postService.getPost(postId);
        return ResponseEntity.ok().body(post);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<Void> updatePost(
            @Valid @RequestBody RequestUpdatePost requestUpdatePost,
            HttpServletRequest httpServletRequest,
            @PathVariable Long postId
    ) {
        postService.updatePost(requestUpdatePost, httpServletRequest, postId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/post/{postId}/like")
    public ResponseEntity<Void> likePost(
            @PathVariable(value = "postId") Long postId
    ) {
        postService.likePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        postService.deletePost(postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

}
