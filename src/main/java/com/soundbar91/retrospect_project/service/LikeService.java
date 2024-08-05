package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.entity.*;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soundbar91.retrospect_project.exception.errorCode.CommentErrorCode.NOT_FOUND_COMMENT;
import static com.soundbar91.retrospect_project.exception.errorCode.PostErrorCode.DUPLICATE_LIKES;
import static com.soundbar91.retrospect_project.exception.errorCode.PostErrorCode.NOT_FOUND_POST;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikesRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void likePost(Long postId, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        postLikesRepository.findByUserAndPost(user, post)
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_LIKES);});

        postLikesRepository.save(new PostLike(user, post));
    }

    public int getLikePostCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        return postLikesRepository.getByPost(post).size();
    }

    @Transactional
    public void likePostCancel(Long postId, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        postLikesRepository.findByUserAndPost(user, post)
                .ifPresent(postLikesRepository::delete);
    }

    @Transactional
    public void likeComment(Long commentId, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_COMMENT));

        commentLikeRepository.findByUserAndComment(user, comment)
                        .ifPresent(e -> {throw new ApplicationException(DUPLICATE_LIKES);});

        commentLikeRepository.save(new CommentLike(user, comment));
    }

    public int getLikeCommentCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_COMMENT));

        return commentLikeRepository.getByComment(comment).size();
    }

    @Transactional
    public void likeCommentCancel(Long commentId, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_COMMENT));

        commentLikeRepository.findByUserAndComment(user, comment)
                .ifPresent(commentLikeRepository::delete);
    }

}
