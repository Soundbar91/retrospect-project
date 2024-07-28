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
import java.util.List;
import java.util.Map;

import static jakarta.persistence.FetchType.EAGER;
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
    private String explanation;

    @Column(nullable = false, columnDefinition = "text")
    private String input_explanation;

    @Column(nullable = false, columnDefinition = "text")
    private String output_explanation;

    @Column(nullable = false)
    @Min(0) @Max(1024)
    private int memory;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Integer> runtime;

    @Column(nullable = false)
    @Min(1) @Max(10)
    private int level;

    @Column(insertable = false)
    @ColumnDefault("0")
    private int submit;

    @Column(insertable = false)
    @ColumnDefault("0")
    private int answer;

    @Column(insertable = false)
    @ColumnDefault("0")
    private int correct;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "json")
    private List<Map<String, String>> example_inout;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "json")
    private List<Map<String, String>> testcase;

    @Column(insertable = false)
    @ColumnDefault("false")
    private boolean isPost;

    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime create_at;

    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modify_at;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "problem", fetch = LAZY)
    private Board board;

    @Builder
    public Problem(String title, String algorithms, String explanation, String input_explanation, String output_explanation,
                   int memory, Map<String, Integer> runtime, int level, List<Map<String, String>> example_inout, List<Map<String, String>> testcase, User user, Board board) {
        this.title = title;
        this.algorithms = algorithms;
        this.explanation = explanation;
        this.input_explanation = input_explanation;
        this.output_explanation = output_explanation;
        this.memory = memory;
        this.runtime = runtime;
        this.level = level;
        this.example_inout = example_inout;
        this.testcase = testcase;
        this.user = user;
        this.board = board;
    }

    public void deleteUser() {
        this.user = null;
    }

    public void updateSubmitInfo(boolean answer, boolean duplicate) {
        this.submit++;
        if (answer) this.answer++;
        if (!duplicate) this.correct++;
    }

    public void updateProblem(RequestUpdateProblem updateProblem) {
       if (updateProblem.title() != null) this.title = updateProblem.title();
       if (updateProblem.algorithms() != null) this.algorithms = updateProblem.algorithms();
       if (updateProblem.explanation() != null) this.explanation = updateProblem.explanation();
       if (updateProblem.input_explanation() != null) this.input_explanation = updateProblem.input_explanation();
       if (updateProblem.output_explanation() != null) this.output_explanation = updateProblem.output_explanation();
       if (updateProblem.memory() != null) this.memory = updateProblem.memory();
       if (updateProblem.runtime() != null) this.runtime = updateProblem.runtime();
       if (updateProblem.level() != null) this.level = updateProblem.level();
       if (updateProblem.isPost() != null) this.isPost = updateProblem.isPost();
       if (updateProblem.example_inout() != null) this.example_inout = updateProblem.example_inout();
       if (updateProblem.testcase() != null) this.testcase = updateProblem.testcase();
    }
}
