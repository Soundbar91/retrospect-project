package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
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

    @OneToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @OneToMany(mappedBy = "board")
    private List<Post> post = new ArrayList<>();

    public Board(Problem problem) {
        this.problem = problem;
    }

}
