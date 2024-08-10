package com.soundbar91.retrospect_project.entity;

import com.soundbar91.retrospect_project.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.entity.keyInstance.Language;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "result")
@NoArgsConstructor(access = PROTECTED)
public class Result {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private int memory;

    @Column(nullable = false)
    private int runtime;

    @Column(nullable = false)
    private Language language;

    @Column(nullable = false)
    private int codeLength;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime submitAt;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Builder
    public Result(Grade grade, int memory, int runtime, Language language,
                  int codeLength, String code, User user, Problem problem) {
        this.grade = grade;
        this.memory = memory;
        this.runtime = runtime;
        this.language = language;
        this.codeLength = codeLength;
        this.code = code;
        this.user = user;
        this.problem = problem;
    }

}
