package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateResult;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseResult;
import com.soundbar91.retrospect_project.service.ResultService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @PostMapping("/problem/{id}")
    public ResponseEntity<ResponseResult> createResult(
            @Valid @RequestBody RequestCreateResult requestCreateResult,
            @PathVariable(value = "id") Long id,
            HttpServletRequest httpServletRequest
    ) {
        ResponseResult result = resultService.createResult(requestCreateResult, httpServletRequest, id);

        return ResponseEntity.ok(result);
    }

}
