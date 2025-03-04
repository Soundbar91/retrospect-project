package com.soundbar91.retrospect_project.result.controller;

import com.soundbar91.retrospect_project.result.controller.dto.RequestSubmit;
import com.soundbar91.retrospect_project.result.controller.dto.ResponseResult;
import com.soundbar91.retrospect_project.result.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.result.entity.keyInstance.Language;

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

    @Operation(summary = "결과 조회", description = """
        CORRECT: 정답, INCORRECT: 오답, COMPILER_ERROR: 컴파일 에러\s
        RUNTIME_ERROR: 런타임 에러, TIME_ACCESS: 시간 초과, MEMORY_ACCESS: 메모리 초과\s
        결과는 AND 연산입니다.
        """)
    @ApiResponse(responseCode = "200", description = "결과 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/result/{resultId}")
    ResponseEntity<ResponseResult> getResult(
        @PathVariable(value = "resultId") Long resultId,
        HttpServletRequest httpServletRequest
    );

    @Operation(summary = "결과 리스트 조회")
    @ApiResponse(responseCode = "200", description = "결과 리스트 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/results")
    ResponseEntity<List<ResponseResult>> getResults(
        @RequestParam(value = "grade", required = false) Grade grade,
        @RequestParam(value = "language", required = false) Language language,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "problemId", required = false) Long problemId
    );

}
