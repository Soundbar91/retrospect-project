package com.soundbar91.retrospect_project.result.repository;

import com.soundbar91.retrospect_project.problem.entity.Problem;
import com.soundbar91.retrospect_project.result.entity.Result;
import com.soundbar91.retrospect_project.user.model.User;
import com.soundbar91.retrospect_project.result.entity.keyInstance.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> getByProblemAndUserAndGrade(Problem problem, User user, Grade grade);
}
