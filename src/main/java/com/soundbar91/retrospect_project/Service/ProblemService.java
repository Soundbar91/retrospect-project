package com.soundbar91.retrospect_project.Service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.repository.ProblemRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseProblem createProblem(RequestCreateProblem requestCreateProblem) {
        User user = userRepository.getByUsername(requestCreateProblem.username());
        if (user == null) System.out.println("등록되지 않은 유저입니다.");
        if (!"admin".equals(user.getRole())) System.out.println("문제 만들기 조건을 달성하지 못했습니다.");

        Problem problem = problemRepository.save(requestCreateProblem.toEntity(user));

        return ResponseProblem.from(problem);
    }

}
