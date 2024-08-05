package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.ProblemRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.soundbar91.retrospect_project.exception.errorCode.ProblemErrorCode.NOT_FOUND_PROBLEM;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_PERMISSION;

@Service
@RequiredArgsConstructor
public class ProblemService {

    @Value("${python.server.url}")
    private String gradingServerUrl;

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    public void createProblem(
            RequestCreateProblem requestCreateProblem,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Problem problem = problemRepository.save(requestCreateProblem.toEntity(user));
        HttpEntity<Map<String, Object>> requestMessage = createRequestMessage(requestCreateProblem.testcase());
        callApiToCreateTestcase(requestMessage, problem.getId());
    }

    public ResponseProblem getProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));

        return ResponseProblem.from(problem);
    }

    public List<ResponseProblem> getProblems(
            String title, Integer level, String algorithms, String mode
    ) {
        StringBuilder jpql = createJpql(title, level, algorithms, mode);

        String[] algorithm = null;
        if (algorithms != null) algorithm = algorithms.split(",");

        TypedQuery<Problem> query = entityManager.createQuery(jpql.toString(), Problem.class);
        queryParameterBinding(title, level, query, algorithm);

        return query.getResultList().stream().map(ResponseProblem::from).toList();
    }

    public List<Map<String, Object>> getTestcase(
        Long problemId, HttpServletRequest httpServletRequest
    ) {
        checkPermission(problemId, httpServletRequest);
        return callApiToGetTestcase(problemId);
    }

    @Transactional
    public void updateProblem(
            Long problemId, RequestUpdateProblem requestUpdateProblem,
            HttpServletRequest httpServletRequest
    ) {
        Problem problem = checkPermission(problemId, httpServletRequest);
        problem.updateProblem(requestUpdateProblem);

        HttpEntity<Map<String, Object>> request = createRequestMessage(requestUpdateProblem.testcase());
        callApiToCreateTestcase(request, problem.getId());

        problemRepository.flush();
    }

    private StringBuilder createJpql(String title, Integer level, String algorithms, String mode) {
        StringBuilder jpql = new StringBuilder("select p from Problem p");
        String separator = mode.equals("true") ? " and " : " or ";
        List<String> algorithmsList = new ArrayList<>();
        List<String> criteria = new ArrayList<>();

        if (title != null) criteria.add(" p.title like :title");
        if (level != null) criteria.add(" p.level = :level");
        if (algorithms != null) {
            for (int i = 0; i < algorithms.split(",").length; i++) {
                algorithmsList.add(" p.algorithms like :algorithms" + i);
            }
        }

        if (!criteria.isEmpty()) jpql.append(" where ");
        for (int i = 0; i < criteria.size(); i++) {
            if (i > 0) jpql.append(" and ");
            jpql.append(criteria.get(i));
        }

        if (!criteria.isEmpty() && !algorithmsList.isEmpty()) jpql.append(" and ");
        if (criteria.isEmpty() && !algorithmsList.isEmpty()) jpql.append(" where ");

        for (int i = 0; i < algorithmsList.size(); i++) {
            if (i > 0) jpql.append(separator);
            jpql.append(algorithmsList.get(i));
        }

        return jpql;
    }

    private void queryParameterBinding(String title, Integer level, TypedQuery<Problem> query, String[] algorithm) {
        if (title != null) query.setParameter("title", "%" + title + "%");
        if (level != null) query.setParameter("level", level);
        if (algorithm != null) {
            for (int i = 0; i < algorithm.length; i++) {
                query.setParameter("algorithms" + i, "%" + algorithm[i] + "%");
            }
        }
    }

    private Problem checkPermission(Long problemId, HttpServletRequest httpServletRequest) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));

        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        if (problem.getUser() != user) throw new ApplicationException(NOT_PERMISSION);

        return problem;
    }

    private HttpEntity<Map<String, Object>> createRequestMessage(List<Map<String, Object>> testcases) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("testcases", testcases);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        return new HttpEntity<>(requestBody, headers);
    }

    private void callApiToCreateTestcase(HttpEntity<Map<String, Object>> entity, Long problemId) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange(
                gradingServerUrl + "/problem/" + problemId + "/testcase",
                HttpMethod.POST,
                entity,
                Void.class
        );
    }

    private List<Map<String, Object>> callApiToGetTestcase(Long problemId) {
        RestTemplate restTemplate = new RestTemplate();

        return (List<Map<String, Object>>) restTemplate.getForEntity(
                gradingServerUrl + "/problem/" + problemId + "/testcase",
                Map.class)
                .getBody().get("testcases");
    }
}
