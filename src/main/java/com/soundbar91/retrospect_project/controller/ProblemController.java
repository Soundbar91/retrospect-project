package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.Service.ProblemService;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/problem")
    public ResponseEntity<ResponseProblem> createProblem(
            @RequestBody RequestCreateProblem requestCreateProblem,
            HttpServletRequest httpServletRequest
    ) {
        ResponseProblem responseProblem = problemService.createProblem(requestCreateProblem, httpServletRequest);
        return ResponseEntity.ok(responseProblem);
    }

    @GetMapping("/problem")
    public ResponseEntity<List<ResponseProblem>> findProblemByParams(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "algorithms", required = false) String algorithms
    ) {
        List<ResponseProblem> problemByParams = problemService.findProblemByParams(id, title, level, algorithms);
        return ResponseEntity.ok(problemByParams);
    }

    @PutMapping("/problem/{id}")
    public ResponseEntity<ResponseProblem> updateProblem(
            @PathVariable Long id,
            @RequestBody RequestUpdateProblem requestUpdateProblem
    ) {
        ResponseProblem responseProblem = problemService.updateProblem(id, requestUpdateProblem);
        return ResponseEntity.ok(responseProblem);
    }
    
}
