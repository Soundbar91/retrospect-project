package com.soundbar91.retrospect_project.problem.repository;

import com.soundbar91.retrospect_project.problem.entity.Problem;
import com.soundbar91.retrospect_project.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByUser(User user);
}
