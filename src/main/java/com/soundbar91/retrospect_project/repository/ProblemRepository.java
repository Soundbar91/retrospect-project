package com.soundbar91.retrospect_project.repository;

import com.soundbar91.retrospect_project.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
}
