package com.soundbar91.retrospect_project.comment.controller;

import com.soundbar91.retrospect_project.comment.controller.dto.RequestCreateComment;
import com.soundbar91.retrospect_project.comment.controller.dto.RequestUpdateComment;
import com.soundbar91.retrospect_project.comment.controller.dto.ResponseComment;
import com.soundbar91.retrospect_project.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentApi {

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

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<ResponseComment>> getComments(
            @PathVariable(value = "postId") Long postID
    ) {
        List<ResponseComment> commentsByPostId = commentService.getComments(postID);
        return ResponseEntity.ok(commentsByPostId);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Void> updateComment(
            @Valid @RequestBody RequestUpdateComment requestUpdateComment,
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        commentService.updateComment(requestUpdateComment, commentId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        commentService.deleteComment(commentId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

}
