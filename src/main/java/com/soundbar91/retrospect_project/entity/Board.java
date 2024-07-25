package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor(access = PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "problemId")
    private Problem problem;

    public Board(Problem problem) {
        this.problem = problem;
    }

}
