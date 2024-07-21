package com.soundbar91.retrospect_project.Service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.ProblemRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soundbar91.retrospect_project.exception.errorCode.ProblemErrorCode.NOT_FOUND_PROBLEM;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_PERMISSION;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseProblem createProblem(RequestCreateProblem requestCreateProblem) {
        User user = userRepository.findByUsername(requestCreateProblem.username())
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        if (!"admin".equals(user.getRole())) throw new ApplicationException(NOT_PERMISSION);

        Problem problem = problemRepository.save(requestCreateProblem.toEntity(user));

        return ResponseProblem.from(problem);
    }

    @Transactional
    public ResponseProblem updateProblem(Long id, RequestUpdateProblem requestUpdateProblem) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));
        problem.updateProblem(requestUpdateProblem);
        problemRepository.flush();

        return ResponseProblem.from(problem);
    }

}
