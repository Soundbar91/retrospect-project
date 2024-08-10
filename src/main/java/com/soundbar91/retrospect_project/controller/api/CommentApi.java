package com.soundbar91.retrospect_project.controller.api;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateComment;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateComment;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseComment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 API")
public interface CommentApi {

    @Operation(summary = "댓글 작성")
    @ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/post/{postId}/comment")
    ResponseEntity<Void> createComment(
            @Valid @RequestBody RequestCreateComment requestCreateComment,
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "댓글 조회")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/post/{postId}/comments")
    ResponseEntity<List<ResponseComment>> getComments(
            @PathVariable(value = "postId") Long postID
    );

    @Operation(summary = "댓글 수정", description = "댓글 작성자만 가능합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(mediaType = "application/json"))
    @PutMapping("/comment/{commentId}")
    ResponseEntity<Void> updateComment(
            @Valid @RequestBody RequestUpdateComment requestUpdateComment,
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "댓글 삭제", description = "댓글 작성자만 가능합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/comment/{commentId}")
    ResponseEntity<Void> deleteComment(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest httpServletRequest
    );

}
