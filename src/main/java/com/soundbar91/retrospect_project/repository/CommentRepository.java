package com.soundbar91.retrospect_project.repository;

import com.soundbar91.retrospect_project.entity.Comment;
import com.soundbar91.retrospect_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByPostId(Long postId);
    List<Comment> findByUser(User user);
}
