package com.soundbar91.retrospect_project.repository;

import com.soundbar91.retrospect_project.entity.Comment;
import com.soundbar91.retrospect_project.entity.CommentLike;
import com.soundbar91.retrospect_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
    List<CommentLike> getByComment(Comment comment);
}
