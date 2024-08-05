package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "post_like")
@NoArgsConstructor(access = PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
    }

}
