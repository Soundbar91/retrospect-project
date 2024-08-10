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

import java.util.*;

import static com.soundbar91.retrospect_project.exception.errorCode.ProblemErrorCode.*;
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

        createProblemRequestValidation(requestCreateProblem);
        Problem problem = problemRepository.save(requestCreateProblem.toEntity(user));

        HttpEntity<Map<String, Object>> requestMessage = createRequestMessage(requestCreateProblem.testcases());
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
        String[] algorithm = null;
        if (algorithms != null) {
            algorithm = algorithms.split(",");
            problemAlgorithmsValidation(algorithm);
        }

        StringBuilder jpql = createJpql(title, level, algorithms, mode);

        TypedQuery<Problem> query = entityManager.createQuery(jpql.toString(), Problem.class);
        queryParameterBinding(title, level, query, algorithm);

        return query.getResultList().stream().map(ResponseProblem::from).toList();
    }

    public List<ResponseProblem> getProblemsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        List<Problem> problems = problemRepository.findByUser(user);
        return problems.stream().map(ResponseProblem::from).toList();
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
        updateProblemRequestValidation(requestUpdateProblem);
        problem.updateProblem(requestUpdateProblem);

        HttpEntity<Map<String, Object>> request = createRequestMessage(requestUpdateProblem.testcases());
        callApiToCreateTestcase(request, problem.getId());

        problemRepository.flush();
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

    private void createProblemRequestValidation(RequestCreateProblem requestCreateProblem) {
        problemTitleValidation(requestCreateProblem.title());
        problemAlgorithmsValidation(requestCreateProblem.algorithms().split(","));
        problemMemoryValidation(requestCreateProblem.memory());
        problemRuntimeValidation(requestCreateProblem.runtime());
        problemLevelValidation(requestCreateProblem.level());
        problemExampleValidation(requestCreateProblem.exampleInOut());
        problemTestcasesValidation(requestCreateProblem.testcases());
    }

    private void updateProblemRequestValidation(RequestUpdateProblem requestUpdateProblem) {
        problemTitleValidation(requestUpdateProblem.title());
        problemAlgorithmsValidation(requestUpdateProblem.algorithms().split(","));
        problemMemoryValidation(requestUpdateProblem.memory());
        problemRuntimeValidation(requestUpdateProblem.runtime());
        problemLevelValidation(requestUpdateProblem.level());
        problemTestcasesValidation(requestUpdateProblem.testcases());
    }

    private void problemTitleValidation(String title) {
        final int minLen = 1, maxLen = 100;

        if (title.length() < minLen || title.length() > maxLen) throw new ApplicationException(INVALID_TITLE);
    }

    private void problemAlgorithmsValidation(String[] algorithms) {
        final Set<String> algorithmSet = Set.of("기하", "구현", "그리디", "문자열", "자료구조", "그래프", "다이나믹 프로그래밍");

        for (String algorithm : algorithms) {
            if (!algorithmSet.contains(algorithm)) throw new ApplicationException(INVALID_ALGORITHM);
        }
    }

    private void problemMemoryValidation(int memory) {
        final int minMemory = 1, maxMemory = 2048;

        if (memory < minMemory || memory > maxMemory) throw new ApplicationException(INVALID_MEMORY);
    }

    private void problemRuntimeValidation(Map<String, Integer> runtime) {
        final Set<String> languageSet = Set.of("cpp", "java", "python");
        final int minRuntime = 100, maxRuntime = 20000;

        Set<String> language = runtime.keySet();
        if (!language.equals(languageSet)) throw new ApplicationException(INVALID_RUNTIME_KEY);

        for (Map.Entry<String, Integer> entry : runtime.entrySet()) {
            int time = entry.getValue();
            if (time < minRuntime || time > maxRuntime) throw new ApplicationException(INVALID_RUNTIME_VALUE);
        }
    }

    private void problemLevelValidation(int level) {
        final int minLevel = 1, maxLevel = 10;

        if (level < minLevel || level > maxLevel) throw new ApplicationException(INVALID_LEVEL);
    }

    private void problemExampleValidation(List<Map<String, Object>> exampleInOut) {
        final Set<String> keySet = Set.of("input", "output");

        for (Map<String, Object> example : exampleInOut) {
            if (!example.keySet().equals(keySet)) throw new ApplicationException(INVALID_EXAMPLE_KEY);
        }
    }

    private void problemTestcasesValidation(List<Map<String, Object>> testcases) {
        final Set<String> keySet = Set.of("input", "output");

        for (Map<String, Object> testcase : testcases) {
            if (!testcase.keySet().equals(keySet)) throw new ApplicationException(INVALID_EXAMPLE_KEY);
        }
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
