package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.service.BoardService;
import com.soundbar91.retrospect_project.service.ProblemService;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final BoardService boardService;

    @PostMapping("/problem")
    public ResponseEntity<Void> createProblem(
            @Valid @RequestBody RequestCreateProblem requestCreateProblem,
            HttpServletRequest httpServletRequest
    ) {
        ResponseProblem responseProblem = problemService.createProblem(requestCreateProblem, httpServletRequest);
        boardService.createBoard(responseProblem.id());
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
            @RequestParam(value = "stand", required = false, defaultValue = "false") String stand
    ) {
        List<ResponseProblem> problems = problemService.getProblems(title, level, algorithms, stand);
        return ResponseEntity.ok(problems);
    }

    @PutMapping("/problem/{id}")
    public ResponseEntity<Void> updateProblem(
            @PathVariable Long id,
            @RequestBody RequestUpdateProblem requestUpdateProblem,
            HttpServletRequest httpServletRequest
    ) {
        problemService.updateProblem(id, requestUpdateProblem, httpServletRequest);
        return ResponseEntity.ok().build();
    }
    
}
