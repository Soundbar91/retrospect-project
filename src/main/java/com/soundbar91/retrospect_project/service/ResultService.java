package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestSubmit;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseResult;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.Result;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.entity.keyInstance.Language;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.ProblemRepository;
import com.soundbar91.retrospect_project.repository.ResultRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.soundbar91.retrospect_project.exception.errorCode.ProblemErrorCode.NOT_FOUND_PROBLEM;
import static com.soundbar91.retrospect_project.exception.errorCode.ResultErrorCode.NOT_FOUND_RESULT;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class ResultService {

    @Value("${python.server.url}")
    private String pythonServerUrl;

    private final ProblemRepository problemRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    public void createResult(
            RequestSubmit requestCreateResult,
            HttpServletRequest httpServletRequest,
            Long problemId
    ) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));

        HttpEntity<Map<String, Object>> request = requestBody(requestCreateResult, problem);
        ResponseEntity<Map> response = responseBody(request);

        Result result = resultRepository.save(requestCreateResult.toEntity(user, problem, response.getBody()));

        List<Result> results = resultRepository.findByUserAndGrade(result.getUser(), result.getGrade());
        boolean answer = result.getGrade().ordinal() == 0;
        boolean duplicate = results.size() > 1;

        problem.updateSubmitInfo(answer, duplicate);
        if (!duplicate) user.solveProblem(problem.getLevel());
    }

    public ResponseResult getResult(Long resultId) {
        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_RESULT));
        return ResponseResult.from(result);
    }

    public List<ResponseResult> getResults(
            Grade grade, Language language,
            String username, Long problemId
    ) {
        User user = null;
        if (username != null) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        }

        Problem problem = null;
        if (problemId != null) {
            problem = problemRepository.findById(problemId)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));
        }

        StringBuilder jpql = getJpql(grade, language, user, problem);
        TypedQuery<Result> query = entityManager.createQuery(jpql.toString(), Result.class);
        if (grade != null) query.setParameter("grade", grade);
        if (language != null) query.setParameter("language", language);
        if (user != null) query.setParameter("user", user);
        if (problem != null) query.setParameter("problem", problem);

        return query.getResultList().stream().map(ResponseResult::from).toList();
    }

    private HttpEntity<Map<String, Object>> requestBody(RequestSubmit requestCreateResult, Problem problem) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("source_code", requestCreateResult.code());
        requestBody.put("language", requestCreateResult.language());
        requestBody.put("memory_limit", problem.getMemory());
        requestBody.put("time_limit", problem.getRuntime().get(String.valueOf(requestCreateResult.language()).toLowerCase()));
        requestBody.put("problem_id", problem.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        return new HttpEntity<>(requestBody, headers);
    }

    private ResponseEntity<Map> responseBody(HttpEntity<Map<String, Object>> entity) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                pythonServerUrl + "/judge",
                HttpMethod.POST,
                entity,
                Map.class
        );
    }

    private static StringBuilder getJpql(Grade grade, Language language, User user, Problem problem) {
        StringBuilder jpql = new StringBuilder("select r from Result r");
        List<String> criteria = new ArrayList<>();

        if (grade != null) criteria.add(" r.grade = :grade");
        if (language != null) criteria.add(" r.language = :language");
        if (user != null) criteria.add(" r.user = :user");
        if (problem != null) criteria.add(" r.problem = :problem");
        if (!criteria.isEmpty()) jpql.append(" where ");

        for (int i = 0; i < criteria.size(); i++) {
            if (i > 0) jpql.append(" and ");
            jpql.append(criteria.get(i));
        }

        return jpql;
    }
}
