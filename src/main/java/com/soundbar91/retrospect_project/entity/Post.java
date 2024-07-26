package com.soundbar91.retrospect_project.entity;

import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdatePost;
import com.soundbar91.retrospect_project.entity.keyInstance.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
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

    @Column(insertable = false)
    @ColumnDefault("0")
    private int likes;

    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime create_at;

    @Column(insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modify_at;

    // TODO 의존성전이 생각해보기 (게시판 - 게시글, 유저 - 게시글)

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment = new ArrayList<>();

    @Builder
    public Post(String title, String content, Category category, Board borad, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.board = borad;
        this.user = user;
    }

    public void updatePost(RequestUpdatePost requestUpdatePost) {
        if (requestUpdatePost.title() != null) this.title = requestUpdatePost.title();
        if (requestUpdatePost.context() != null) this.content = requestUpdatePost.context();
        if (requestUpdatePost.category() != null) this.category = requestUpdatePost.category();
        if (requestUpdatePost.likes() != null) this.likes = requestUpdatePost.likes();
    }

}
