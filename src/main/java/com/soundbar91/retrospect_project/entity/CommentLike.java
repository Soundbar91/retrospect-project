package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "comment_like")
@NoArgsConstructor(access = PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

}
