package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateComment;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdateComment;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseComment;
import com.soundbar91.retrospect_project.entity.Comment;
import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.CommentRepository;
import com.soundbar91.retrospect_project.repository.PostRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.soundbar91.retrospect_project.exception.errorCode.CommentErrorCode.NOT_FOUND_COMMENT;
import static com.soundbar91.retrospect_project.exception.errorCode.PostErrorCode.NOT_FOUND_POST;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_PERMISSION;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createComment(
            RequestCreateComment requestCreateComment,
            Long postId,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        Comment comment = requestCreateComment.toEntity(user, post);
        commentRepository.save(comment);
    }

    public List<ResponseComment> getComments(Long postId) {
        return commentRepository.findCommentByPostId(postId)
                .stream().map(ResponseComment::from).toList();
    }

    @Transactional
    public void updateComment(
            RequestUpdateComment requestUpdateComment,
            Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        Comment comment = valid(commentId, httpServletRequest);
        comment.updateComment(requestUpdateComment);
        commentRepository.flush();
    }

    @Transactional
    public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_COMMENT));

        comment.likeComment();
        commentRepository.flush();
    }

    @Transactional
    public void deleteComment(Long commentId, HttpServletRequest httpServletRequest) {
        valid(commentId, httpServletRequest);
        commentRepository.deleteById(commentId);
    }

    private Comment valid(Long commentId, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_COMMENT));

        if (comment.getUser() != user) throw new ApplicationException(NOT_PERMISSION);
        return comment;
    }
}
