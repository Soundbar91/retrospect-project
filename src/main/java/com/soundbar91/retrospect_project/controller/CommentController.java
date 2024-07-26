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

    @PostMapping("/board/{boardId}/post/{postID}/comment")
    public ResponseEntity<Void> createComment(
            @Valid @RequestBody RequestCreateComment requestCreateComment,
            @PathVariable(value = "postID") Long postId,
            HttpServletRequest httpServletRequest
    ) {
        commentService.createComment(requestCreateComment, postId, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/board/{boardId}/post/{postID}/comment")
    public ResponseEntity<List<ResponseComment>> findCommentsByPostId(
            @PathVariable(value = "postID") Long postID
    ) {
        List<ResponseComment> commentsByPostId = commentService.findCommentsByPostId(postID);
        return ResponseEntity.ok(commentsByPostId);
    }

    @PutMapping("/board/{boardId}/post/{postID}/comment/{commentID}")
    public ResponseEntity<Void> updateComment(
            @Valid @RequestBody RequestUpdateComment requestUpdateComment,
            @PathVariable(value = "commentID") Long commentId
    ) {
        commentService.updateComment(requestUpdateComment, commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/board/{boardId}/post/{postID}/comment/{commentID}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(value = "commentID") Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

}
