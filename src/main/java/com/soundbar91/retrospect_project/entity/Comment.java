package com.soundbar91.retrospect_project.entity;

import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateComment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.EAGER;
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

    @NotNull
    @Column(nullable = false, columnDefinition = "TEXT")
    private String context;

    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime create_at;

    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modify_at;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public Comment(String context, User user, Post post) {
        this.context = context;
        this.user = user;
        this.post = post;
    }

    public void updateComment(RequestUpdateComment requestUpdateComment) {
        this.context = requestUpdateComment.context();
    }

    public void deleteUser() {
        this.user = null;
    }

}
