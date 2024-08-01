package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.dto.response.ResponseResult;
import com.soundbar91.retrospect_project.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.entity.keyInstance.Language;
import com.soundbar91.retrospect_project.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/result/{resultId}")
    public ResponseEntity<ResponseResult> getResult(
            @PathVariable(value = "resultId") Long resultId
    ) {
        ResponseResult result = resultService.getResult(resultId);
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
