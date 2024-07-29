package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateComment;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateComment;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseComment;
import com.soundbar91.retrospect_project.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<Void> createComment(
            @Valid @RequestBody RequestCreateComment requestCreateComment,
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        commentService.createComment(requestCreateComment, postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<ResponseComment>> findCommentsByPostId(
            @PathVariable(value = "postId") Long postID
    ) {
        List<ResponseComment> commentsByPostId = commentService.findCommentsByPostId(postID);
        return ResponseEntity.ok(commentsByPostId);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Void> updateComment(
            @Valid @RequestBody RequestUpdateComment requestUpdateComment,
            @PathVariable(value = "commentId") Long commentId
    ) {
        commentService.updateComment(requestUpdateComment, commentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/comment/{commentId}/like")
    public ResponseEntity<Void> likeComment(
            @PathVariable(value = "commentId") Long commentId
    ) {
        commentService.likeComment(commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(value = "commentId") Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

}
