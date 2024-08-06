package com.soundbar91.retrospect_project.repository;

import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> getByUser(User user);
}
