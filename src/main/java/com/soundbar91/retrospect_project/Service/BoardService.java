package com.soundbar91.retrospect_project.Service;

import com.soundbar91.retrospect_project.entity.Board;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.BoardRepository;
import com.soundbar91.retrospect_project.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.soundbar91.retrospect_project.exception.errorCode.ProblemErrorCode.NOT_FOUND_PROBLEM;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ProblemRepository problemRepository;

    public void createBoard(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));

        boardRepository.save(new Board(problem));
    }
    
}
