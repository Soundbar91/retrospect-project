package com.soundbar91.retrospect_project.controller.api;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "알고리즘 문제 API")
public interface ProblemApi {

    @Operation(summary = "알고리즘 문제 만들기", description = """
            알고리즘의 경우 기하, 구현, 그리디, 문자열, 자료구조, 그래프, 그리고 다이나믹 프로그래밍 중에서 입력하셔야 하며,
            콤마를 통해 연결하셔야 합니다.(ex: 기하, 구현)
            """)
    @ApiResponse(responseCode = "200", description = "문제 생성 성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/problem")
    ResponseEntity<Void> createProblem(
            @Valid @RequestBody RequestCreateProblem requestCreateProblem,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "번호로 조회")
    @ApiResponse(responseCode = "200", description = "문제 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/problem/{problemId}")
    ResponseEntity<ResponseProblem> getProblem(
            @PathVariable(value = "problemId") Long problemId
    );

    @Operation(summary = "필터 설정으로 조회", description = """
            모드는 true와 false 중 하나의 값을 입력하셔야 합니다.
            (true : 입력한 알고리즘을 모두 포함하는 문제, false : 입력한 알고리즘 중 하나라도 포함한 문제)
            \n알고리즘의 경우 기하, 구현, 그리디, 문자열, 자료구조, 그래프, 그리고 다이나믹 프로그래밍 중에서 입력하셔야 하며,
            콤마를 통해 연결하셔야 합니다.(ex: 기하, 구현)
            """)
    @ApiResponse(responseCode = "200", description = "문제 리스트 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/problems")
    ResponseEntity<List<ResponseProblem>> getProblems(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "algorithms", required = false) String algorithms,
            @RequestParam(value = "mode", required = false, defaultValue = "false") String mode
    );

    @Operation(summary = "테스트 케이스 조회", description = "문제 제작자만 가능합니다.")
    @ApiResponse(responseCode = "200", description = "테스트 케이스 조회 성공", content = @Content(mediaType = "application/json"))
    @GetMapping("/problem/{problemId}/testcase")
    ResponseEntity<List<Map<String, Object>>> getTestcase(
            @PathVariable(value = "problemId") Long problemId,
            HttpServletRequest httpServletRequest
    );

    @Operation(summary = "문제 업데이트", description = "문제 제작자만 가능합니다.")
    @ApiResponse(responseCode = "200", description = "문제 업데이트 성공", content = @Content(mediaType = "application/json"))
    @PutMapping("/problem/{problemId}")
    ResponseEntity<Void> updateProblem(
            @PathVariable(value = "problemId") Long problemId,
            @RequestBody RequestUpdateProblem requestUpdateProblem,
            HttpServletRequest httpServletRequest
    );
    
}
