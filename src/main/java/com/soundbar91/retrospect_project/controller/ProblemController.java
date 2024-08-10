package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.api.ProblemApi;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import com.soundbar91.retrospect_project.service.ProblemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProblemController implements ProblemApi {

    private final ProblemService problemService;

    @PostMapping("/problem")
    public ResponseEntity<Void> createProblem(
            @Valid @RequestBody RequestCreateProblem requestCreateProblem,
            HttpServletRequest httpServletRequest
    ) {
        problemService.createProblem(requestCreateProblem, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<ResponseProblem> getProblem(
            @PathVariable(value = "problemId") Long problemId
    ) {
        ResponseProblem problem = problemService.getProblem(problemId);
        return ResponseEntity.ok(problem);
    }

    @GetMapping("/problems")
    public ResponseEntity<List<ResponseProblem>> getProblems(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "algorithms", required = false) String algorithms,
            @RequestParam(value = "mode", required = false, defaultValue = "false") String mode
    ) {
        List<ResponseProblem> problems = problemService.getProblems(title, level, algorithms, mode);
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/problem/{problemId}/testcase")
    public ResponseEntity<List<Map<String, Object>>> getTestcase(
        @PathVariable(value = "problemId") Long problemId,
        HttpServletRequest httpServletRequest
    ) {
        List<Map<String, Object>> testcase = problemService.getTestcase(problemId, httpServletRequest);
        return ResponseEntity.ok(testcase);
    }

    @PutMapping("/problem/{problemId}")
    public ResponseEntity<Void> updateProblem(
            @PathVariable(value = "problemId") Long problemId,
            @RequestBody RequestUpdateProblem requestUpdateProblem,
            HttpServletRequest httpServletRequest
    ) {
        problemService.updateProblem(problemId, requestUpdateProblem, httpServletRequest);
        return ResponseEntity.ok().build();
    }

}
