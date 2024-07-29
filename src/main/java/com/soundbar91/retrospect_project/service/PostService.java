package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreatePost;
import com.soundbar91.retrospect_project.controller.dto.request.RequestUpdatePost;
import com.soundbar91.retrospect_project.controller.dto.response.ResponsePost;
import com.soundbar91.retrospect_project.entity.Board;
import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.entity.keyInstance.Category;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.BoardRepository;
import com.soundbar91.retrospect_project.repository.PostRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.soundbar91.retrospect_project.exception.errorCode.BoardErrorCode.NOT_FOUND_BOARD;
import static com.soundbar91.retrospect_project.exception.errorCode.PostErrorCode.NOT_FOUND_POST;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_PERMISSION;

@Service
@RequiredArgsConstructor
public class PostService {

    private final EntityManager entityManager;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPost(
            RequestCreatePost requestCreatePost,
            Long boardId,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_BOARD));

        postRepository.save(requestCreatePost.toEntity(user, board));
    }

    public List<ResponsePost> findPosts(
            Long boardId, String username, Category category
    ) {
        Board board = null;
        if (boardId != null) {
            board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_BOARD));
        }

        User user = null;
        if (username != null) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        }

        StringBuilder jpql = getJpql(board, user, category);

        TypedQuery<Post> query = entityManager.createQuery(jpql.toString(), Post.class);
        if (user != null) query.setParameter("user", user);
        if (board != null) query.setParameter("board", board);
        if (category != null) query.setParameter("category", category);

        return query.getResultList().stream().map(ResponsePost::from).toList();
    }

    public ResponsePost findPost(Long postId) {
        return postRepository.findById(postId)
                .map(ResponsePost::from)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));
    }

    @Transactional
    public void updatePost(
            RequestUpdatePost requestUpdatePost,
            HttpServletRequest httpServletRequest,
            Long postId
    ) {
        Post post = valid(postId, httpServletRequest);
        post.updatePost(requestUpdatePost);
        postRepository.flush();
    }

    @Transactional
    public void deletePost(Long postId, HttpServletRequest httpServletRequest) {
        valid(postId, httpServletRequest);
        postRepository.deleteById(postId);
    }

    private StringBuilder getJpql(Board board, User user, Category category) {
        StringBuilder jpql = new StringBuilder("select p from Post p");
        List<String> criteria = new ArrayList<>();

        if (board != null) criteria.add(" p.board = :board");
        if (user != null) criteria.add(" p.user = :user");
        if (category != null) criteria.add(" p.category = :category");
        if (!criteria.isEmpty()) jpql.append(" where ");

        for (int i = 0; i < criteria.size(); i++) {
            if (i > 0) jpql.append(" and ");
            jpql.append(criteria.get(i));
        }

        return jpql;
    }

    private Post valid(Long postId, HttpServletRequest httpServletRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (post.getUser() != user) throw new ApplicationException(NOT_PERMISSION);
        return post;
    }
}
