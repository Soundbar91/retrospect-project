package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "post_id")
    private Post post;


}
