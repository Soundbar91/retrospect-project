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

    @GetMapping("/board/{boardId}/post")
    public ResponseEntity<List<ResponsePost>> getPostByParam(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "category", required = false) Category category
    ) {
        List<ResponsePost> postByParam = postService.findPostByParam(username, category);
        return ResponseEntity.ok(postByParam);
    }

    @GetMapping("/board/{boardId}/post/{postId}")
    public ResponseEntity<ResponsePost> getPostByPostId(
            @PathVariable(value = "postId") Long postId
    ) {
        ResponsePost post = postService.findPostByPostId(postId);
        return ResponseEntity.ok().body(post);
    }

    @PutMapping("/board/{boardId}/post/{postId}")
    public ResponseEntity<Void> updatePost(
            @Valid @RequestBody RequestUpdatePost requestUpdatePost,
            @PathVariable Long postId
    ) {
        postService.updatePost(requestUpdatePost, postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/board/{boardId}/post/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable(value = "postId") Long postId
    ) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

}
