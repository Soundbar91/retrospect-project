package com.soundbar91.retrospect_project.like.repository;

import com.soundbar91.retrospect_project.like.entity.PostLike;
import com.soundbar91.retrospect_project.post.entity.Post;
import com.soundbar91.retrospect_project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
    List<PostLike> getByPost(Post post);
}
