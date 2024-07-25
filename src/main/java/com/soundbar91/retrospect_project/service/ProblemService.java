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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import static com.soundbar91.retrospect_project.exception.errorCode.ProblemErrorCode.NOT_FOUND_PROBLEM;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_PERMISSION;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    public ResponseProblem createProblem(
            RequestCreateProblem requestCreateProblem,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        if (!"admin".equals(user.getRole())) throw new ApplicationException(NOT_PERMISSION);

        Problem problem = problemRepository.save(requestCreateProblem.toEntity(user));

        return ResponseProblem.from(problem);
    }

    public List<ResponseProblem> findProblemByParams(
            Long id, String title, Integer level, String algorithms, String stand
    ) {
        StringBuilder jpql = getJpql(id, title, level, algorithms, stand);

        String[] algorithm = null;
        if (algorithms != null) algorithm = algorithms.split(",");

        TypedQuery<Problem> query = entityManager.createQuery(jpql.toString(), Problem.class);
        if (id != null) query.setParameter("id", id);
        if (title != null) query.setParameter("title", "%" + title + "%");
        if (level != null) query.setParameter("level", level);
        if (algorithms != null) {
            for (int i = 0; i < algorithm.length; i++) {
                query.setParameter("algorithms" + i, "%" + algorithm[i] + "%");
                System.out.println(algorithm[i]);
            }
        }

        return query.getResultList().stream().map(ResponseProblem::from).toList();
    }

    @Transactional
    public ResponseProblem updateProblem(Long id, RequestUpdateProblem requestUpdateProblem) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));
        problem.updateProblem(requestUpdateProblem);
        problemRepository.flush();

        return ResponseProblem.from(problem);
    }

    private static StringBuilder getJpql(Long id, String title, Integer level, String algorithms, String stand) {
        StringBuilder jpql = new StringBuilder("select p from Problem p");
        String str = stand.equals("true") ? " and " : " or ";
        List<String> algorithmsList = new ArrayList<>();
        List<String> criteria = new ArrayList<>();

        if (id != null) criteria.add(" p.id = :id");
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
            if (i > 0) jpql.append(str);
            jpql.append(algorithmsList.get(i));
        }

        return jpql;
    }
}
