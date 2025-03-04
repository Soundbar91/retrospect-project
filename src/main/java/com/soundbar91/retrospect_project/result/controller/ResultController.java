package com.soundbar91.retrospect_project.result.controller;

import com.soundbar91.retrospect_project.result.controller.dto.RequestSubmit;
import com.soundbar91.retrospect_project.result.controller.dto.ResponseResult;
import com.soundbar91.retrospect_project.result.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.result.entity.keyInstance.Language;
import com.soundbar91.retrospect_project.result.service.ResultService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResultController implements ResultApi {

    private final ResultService resultService;

    @PostMapping("/problem/{problemId}/result")
    public ResponseEntity<ResponseResult> createResult(
        @Valid @RequestBody RequestSubmit requestCreateResult,
        @PathVariable(value = "problemId") Long problemId,
        HttpServletRequest httpServletRequest
    ) {
        ResponseResult result = resultService.createResult(requestCreateResult, httpServletRequest, problemId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/result/{resultId}")
    public ResponseEntity<ResponseResult> getResult(
        @PathVariable(value = "resultId") Long resultId,
        HttpServletRequest httpServletRequest
    ) {
        ResponseResult result = resultService.getResult(resultId, httpServletRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/results")
    public ResponseEntity<List<ResponseResult>> getResults(
        @RequestParam(value = "grade", required = false) Grade grade,
        @RequestParam(value = "language", required = false) Language language,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "problemId", required = false) Long problemId
    ) {
        List<ResponseResult> results = resultService.getResults(grade, language, username, problemId);
        return ResponseEntity.ok(results);
    }

}
