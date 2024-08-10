package com.soundbar91.retrospect_project.controller.api;

import com.soundbar91.retrospect_project.controller.dto.request.RequestSubmit;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseResult;
import com.soundbar91.retrospect_project.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.entity.keyInstance.Language;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "결과 API")
public interface ResultApi {
    
    @Operation(summary = "코드 제출", description = "제출 언어의 경우 CPP, JAVA, PYTHON 중 하나의 값을 입력해야 합니다.")
    @ApiResponse(responseCode = "200", description = "코드 제출 성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/problem/{problemId}/result")
    ResponseEntity<ResponseResult> createResult(
            @Valid @RequestBody RequestSubmit requestCreateResult,
            @PathVariable(value = "problemId") Long problemId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "결과 번호 조회")
    @ApiResponse(responseCode = "200", description = "결과 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/result/{resultId}")
    ResponseEntity<ResponseResult> getResult(
            @PathVariable(value = "resultId") Long resultId
    );

    @Operation(summary = "결과 필터 조회")
    @ApiResponse(responseCode = "200", description = "결과 리스트 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/results")
    ResponseEntity<List<ResponseResult>> getResults(
            @RequestParam(value = "grade", required = false) Grade grade,
            @RequestParam(value = "language", required = false) Language language,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "problemId", required = false) Long problemId
    );

}
