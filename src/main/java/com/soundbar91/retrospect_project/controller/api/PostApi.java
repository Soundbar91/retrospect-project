package com.soundbar91.retrospect_project.controller.api;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreatePost;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdatePost;
import com.soundbar91.retrospect_project.controller.dto.response.ResponsePost;
import com.soundbar91.retrospect_project.entity.keyInstance.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글 API")
public interface PostApi {
    
    @Operation(summary = "게시글 작성", description = """
            카테고리의 경우 ERROR, QUESTION, COUNTEREXAMPLE 중 하나의 값을 입력해야합니다.
            """)
    @ApiResponse(responseCode = "200", description = "게시글 작성 성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/problem/{problemId}/post")
    ResponseEntity<Void> createPost(
            @Valid @RequestBody RequestCreatePost requestCreatePost,
            @PathVariable(value = "problemId") Long problemId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "게시글 리스트 조회", description = """
            ERROR: 오류, QUESTION: 질문, COUNTEREXAMPLE: 반례
            """)
    @ApiResponse(responseCode = "200", description = "게시글 리스트 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/posts")
    ResponseEntity<List<ResponsePost>> getPosts(
            @RequestParam(value = "problemId", required = false) Long problemId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "category", required = false) Category category
    );

    @Operation(summary = "게시글 조회")
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/post/{postId}")
    ResponseEntity<ResponsePost> getPost(
            @PathVariable(value = "postId") Long postId
    );

    @Operation(summary = "게시글 업데이트", description = "게시글 작성자만 가능합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(mediaType = "application/json"))
    @PutMapping("/post/{postId}")
    ResponseEntity<Void> updatePost(
            @Valid @RequestBody RequestUpdatePost requestUpdatePost,
            HttpServletRequest httpServletRequest,
            @PathVariable(value = "postId") Long postId
    );

    @Operation(summary = "게시글 삭제", description = "게시글 작성자만 가능합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/post/{postId}")
    ResponseEntity<Void> deletePost(
            @PathVariable(value = "postId") Long postId,
            HttpServletRequest httpServletRequest
    );
    
}
