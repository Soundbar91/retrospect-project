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

    @PostMapping("/board/{boardId}/post")
    public ResponseEntity<Void> createPost(
            @Valid @RequestBody RequestCreatePost requestCreatePost,
            @PathVariable Long boardId,
            HttpServletRequest httpServletRequest
    ) {
        postService.createPost(requestCreatePost, boardId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<ResponsePost>> getPosts(
            @RequestParam(value = "boardId", required = false) Long boardId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "category", required = false) Category category
    ) {
        List<ResponsePost> postByParam = postService.findPosts(boardId, username, category);
        return ResponseEntity.ok(postByParam);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponsePost> getPostByPostId(
            @PathVariable(value = "postId") Long postId
    ) {
        ResponsePost post = postService.findPost(postId);
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

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        postService.deletePost(postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

}
