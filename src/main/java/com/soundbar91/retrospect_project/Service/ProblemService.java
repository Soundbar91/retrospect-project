package com.soundbar91.retrospect_project.Service;

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
    public ResponseProblem createProblem(RequestCreateProblem requestCreateProblem) {
        User user = userRepository.findByUsername(requestCreateProblem.username())
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        if (!"admin".equals(user.getRole())) throw new ApplicationException(NOT_PERMISSION);

        Problem problem = problemRepository.save(requestCreateProblem.toEntity(user));

        return ResponseProblem.from(problem);
    }

    public List<ResponseProblem> findProblemByParams(
            Long id, String title, Integer level, String algorithms
    ) {
        StringBuilder jpql = getBuilder(id, title, level, algorithms);

        TypedQuery<Problem> query = entityManager.createQuery(jpql.toString(), Problem.class);
        if (id != null) query.setParameter("id", id);
        if (title != null) query.setParameter("title", "%" + title + "%");
        if (level != null) query.setParameter("level", level);
        if (algorithms != null) query.setParameter("algorithms", "%" + algorithms + "%");
        List<Problem> resultList = query.getResultList();

        return resultList.stream().map(ResponseProblem::from).toList();
    }

    @Transactional
    public ResponseProblem updateProblem(Long id, RequestUpdateProblem requestUpdateProblem) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));
        problem.updateProblem(requestUpdateProblem);
        problemRepository.flush();

        return ResponseProblem.from(problem);
    }

    private static StringBuilder getBuilder(Long id, String title, Integer level, String algorithms) {
        StringBuilder jpql = new StringBuilder("select p from Problem p");
        List<String> criteria = new ArrayList<>();

        if (id != null) criteria.add(" p.id = :id");
        if (title != null) criteria.add(" p.title like :title");
        if (level != null) criteria.add(" p.level = :level");
        if (algorithms != null) criteria.add(" p.algorithms like :algorithms");

        if (!criteria.isEmpty()) jpql.append(" where ");

        for (int i = 0; i < criteria.size(); i++) {
            if (i > 0) jpql.append(" and ");
            jpql.append(criteria.get(i));
        }

        return jpql;
    }
}
