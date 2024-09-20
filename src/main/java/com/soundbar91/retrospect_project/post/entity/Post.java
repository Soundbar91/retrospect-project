package com.soundbar91.retrospect_project.post.entity;

import com.soundbar91.retrospect_project.post.controller.dto.RequestUpdatePost;
import com.soundbar91.retrospect_project.comment.entity.Comment;
import com.soundbar91.retrospect_project.like.entity.PostLike;
import com.soundbar91.retrospect_project.post.entity.keyInstance.Category;
import com.soundbar91.retrospect_project.problem.entity.Problem;
import com.soundbar91.retrospect_project.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifyAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "post", fetch = LAZY, orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = LAZY, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();

    @Builder
    public Post(String title, String content, Category category, User user, Problem problem) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
        this.problem = problem;
    }

    public void updatePost(RequestUpdatePost requestUpdatePost) {
        this.title = requestUpdatePost.title();
        this.content = requestUpdatePost.context();
        this.category = requestUpdatePost.category();
    }

}
