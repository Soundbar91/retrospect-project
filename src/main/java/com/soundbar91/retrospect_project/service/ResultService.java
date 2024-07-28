package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateResult;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseResult;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.Result;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.ProblemRepository;
import com.soundbar91.retrospect_project.repository.ResultRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.soundbar91.retrospect_project.exception.errorCode.ProblemErrorCode.NOT_FOUND_PROBLEM;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class ResultService {

    @Value("${python.server.url}")
    private String pythonServerUrl;

    private final ResultRepository resultRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseResult createResult(
            RequestCreateResult requestCreateResult,
            HttpServletRequest httpServletRequest,
            Long problemId
    ) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("source_code", requestCreateResult.code());
        requestBody.put("language", requestCreateResult.language());
        requestBody.put("test_cases", problem.getTestcase());
        requestBody.put("memory_limit", problem.getMemory());
        requestBody.put("time_limit", problem.getRuntime().get(String.valueOf(requestCreateResult.language()).toLowerCase()));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                pythonServerUrl + "/judge",
                HttpMethod.POST,
                entity,
                Map.class
        );

        Result result = resultRepository.save(requestCreateResult.toEntity(user, problem, response.getBody()));
        boolean answer = result.getGrade().ordinal() == 0;
        List<Result> results = resultRepository.findByUserAndGrade(result.getUser(), result.getGrade());
        boolean duplicate = results.size() > 1;
        problem.updateSubmitInfo(answer, duplicate);

        if (!duplicate) user.solveProblem(problem.getLevel());

        return ResponseResult.from(result);
    }

}
