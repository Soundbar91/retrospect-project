package com.soundbar91.retrospect_project.comment.repository;

import com.soundbar91.retrospect_project.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getByPostId(Long postId);
}
