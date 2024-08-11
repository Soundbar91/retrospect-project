package com.soundbar91.retrospect_project.comment.entity;

import com.soundbar91.retrospect_project.comment.controller.dto.RequestUpdateComment;
import com.soundbar91.retrospect_project.like.entity.CommentLike;
import com.soundbar91.retrospect_project.post.entity.Post;
import com.soundbar91.retrospect_project.user.entity.User;
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
@Table(name = "comment")
@NoArgsConstructor(access = PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String context;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifyAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "comment", fetch = LAZY, orphanRemoval = true)
    private List<CommentLike> likes = new ArrayList<>();

    @Builder
    public Comment(String context, User user, Post post) {
        this.context = context;
        this.user = user;
        this.post = post;
    }

    public void updateComment(RequestUpdateComment requestUpdateComment) {
        this.context = requestUpdateComment.context();
    }

}
