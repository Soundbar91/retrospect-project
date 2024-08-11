package com.soundbar91.retrospect_project.post.repository;

import com.soundbar91.retrospect_project.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
