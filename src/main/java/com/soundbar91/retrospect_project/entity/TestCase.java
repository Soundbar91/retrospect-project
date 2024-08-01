package com.soundbar91.retrospect_project.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "testcase")
@NoArgsConstructor(access = PROTECTED)
public class TestCase {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "json")
    private List<Map<String, String>> testcase;

    @OneToOne(fetch = FetchType.EAGER)
    private Problem problem;

    public void updateTestCase(List<Map<String, String>> testcase) {
        this.testcase = testcase;
    }

    public TestCase(List<Map<String, String>> testcase, Problem problem) {
        this.testcase = testcase;
        this.problem = problem;
    }

}
