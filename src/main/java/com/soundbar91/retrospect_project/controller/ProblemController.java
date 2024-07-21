package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.Service.ProblemService;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/problem")
    public ResponseEntity<ResponseProblem> createProblem(
            @RequestBody RequestCreateProblem requestCreateProblem
    ) {
        ResponseProblem responseProblem = problemService.createProblem(requestCreateProblem);
        return ResponseEntity.ok(responseProblem);
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
