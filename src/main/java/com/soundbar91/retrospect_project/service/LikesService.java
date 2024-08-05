package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.LikesRepository;
import com.soundbar91.retrospect_project.repository.PostRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soundbar91.retrospect_project.exception.errorCode.PostErrorCode.DUPLICATE_LIKES;
import static com.soundbar91.retrospect_project.exception.errorCode.PostErrorCode.NOT_FOUND_POST;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public void likePost(Long postId, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        likesRepository.findByUserAndPost(user, post)
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_LIKES);});

        post.likePost();
        postRepository.flush();
    }

    public int getLikesCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        return likesRepository.getByPost(post).size();
    }

    @Transactional
    public void likeCancel(Long postId, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        likesRepository.findByUserAndPost(user, post)
                .ifPresent(likesRepository::delete);
    }

}
