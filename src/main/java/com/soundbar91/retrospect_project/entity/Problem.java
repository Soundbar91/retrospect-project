package com.soundbar91.retrospect_project.entity;

import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateProblem;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "problem")
@NoArgsConstructor(access = PROTECTED)
public class Problem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String algorithms;

    @Column(nullable = false, columnDefinition = "text")
    private String problemExplanation;

    @Column(nullable = false, columnDefinition = "text")
    private String inputExplanation;

    @Column(nullable = false, columnDefinition = "text")
    private String outputExplanation;

    @Column(nullable = false)
    @Min(1) @Max(2048)
    private int memory;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Integer> runtime;

    @Column(nullable = false)
    @Min(1) @Max(10)
    private int level;

    @Column(nullable = false, insertable = false)
    @ColumnDefault("0")
    private int submit;

    @Column(nullable = false, insertable = false)
    @ColumnDefault("0")
    private int answer;

    @Type(JsonType.class)
    @Column(name = "example_inout", nullable = false, columnDefinition = "json")
    private List<Map<String, Object>> exampleInOut;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifyAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "problem", fetch = LAZY)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Problem(String title, String algorithms, String problemExplanation, String inputExplanation, String outputExplanation,
                   int memory, Map<String, Integer> runtime, int level, List<Map<String, Object>> exampleInOut, User user) {
        this.title = title;
        this.algorithms = algorithms;
        this.problemExplanation = problemExplanation;
        this.inputExplanation = inputExplanation;
        this.outputExplanation = outputExplanation;
        this.memory = memory;
        this.runtime = runtime;
        this.level = level;
        this.exampleInOut = exampleInOut;
        this.user = user;
    }

    public void updateProblem(RequestUpdateProblem updateProblem) {
        this.title = updateProblem.title();
        this.algorithms = updateProblem.algorithms();
        this.problemExplanation = updateProblem.problemExplanation();
        this.inputExplanation = updateProblem.inputExplanation();
        this.outputExplanation = updateProblem.outputExplanation();
        this.memory = updateProblem.memory();
        this.runtime = updateProblem.runtime();
        this.level = updateProblem.level();
        this.exampleInOut = updateProblem.exampleInOut();
    }

    public void updateSubmitInfo(boolean answer) {
        this.submit++;
        if (answer) this.answer++;
    }

}
