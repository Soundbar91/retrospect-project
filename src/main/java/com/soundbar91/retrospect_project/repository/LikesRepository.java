package com.soundbar91.retrospect_project.repository;

import com.soundbar91.retrospect_project.entity.Likes;
import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer> {
    Optional<Likes> findByUserAndPost(User user, Post post);
    List<Likes> getByPost(Post post);
}
