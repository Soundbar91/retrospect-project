package com.soundbar91.retrospect_project.post.service;

import com.soundbar91.retrospect_project.post.controller.dto.RequestCreatePost;
import com.soundbar91.retrospect_project.post.controller.dto.RequestUpdatePost;
import com.soundbar91.retrospect_project.post.controller.dto.ResponsePost;
import com.soundbar91.retrospect_project.post.entity.Post;
import com.soundbar91.retrospect_project.problem.entity.Problem;
import com.soundbar91.retrospect_project.user.model.User;
import com.soundbar91.retrospect_project.post.entity.keyInstance.Category;
import com.soundbar91.retrospect_project.global.exception.ApplicationException;
import com.soundbar91.retrospect_project.post.repository.PostRepository;
import com.soundbar91.retrospect_project.problem.repository.ProblemRepository;
import com.soundbar91.retrospect_project.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.soundbar91.retrospect_project.global.exception.errorCode.PostErrorCode.INVALID_TITLE_LEN;
import static com.soundbar91.retrospect_project.global.exception.errorCode.PostErrorCode.NOT_FOUND_POST;
import static com.soundbar91.retrospect_project.global.exception.errorCode.ProblemErrorCode.NOT_FOUND_PROBLEM;
import static com.soundbar91.retrospect_project.global.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.global.exception.errorCode.UserErrorCode.NOT_PERMISSION;

@Service
@RequiredArgsConstructor
public class PostService {

    private final EntityManager entityManager;
    private final PostRepository postRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPost(
            RequestCreatePost requestCreatePost,
            Long problemId,
            HttpServletRequest httpServletRequest
    ) {
        postTitleValidation(requestCreatePost.title());

        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM));

        postRepository.save(requestCreatePost.toEntity(user, problem));
    }

    public List<ResponsePost> getPosts(
            Long problemId, String username, Category category
    ) {
        StringBuilder jpql = createJpql(problemId, username, category);

        Problem problem = problemId != null ?
                problemRepository.findById(problemId).orElseThrow(() -> new ApplicationException(NOT_FOUND_PROBLEM)) : null;

        User user = username != null ?
                userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(NOT_FOUND_USER)) : null;

        TypedQuery<Post> query = entityManager.createQuery(jpql.toString(), Post.class);
        queryParameterBinding(category, user, query, problem);

        return query.getResultList().stream().map(ResponsePost::from).toList();
    }

    public ResponsePost getPost(Long postId) {
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
        Post post = checkPermission(postId, httpServletRequest);
        postTitleValidation(requestUpdatePost.title());
        post.updatePost(requestUpdatePost);
        postRepository.flush();
    }

    @Transactional
    public void deletePost(Long postId, HttpServletRequest httpServletRequest) {
        checkPermission(postId, httpServletRequest);
        postRepository.deleteById(postId);
    }

    private StringBuilder createJpql(Long problemId, String username, Category category) {
        StringBuilder jpql = new StringBuilder("select p from Post p");
        List<String> criteria = new ArrayList<>();

        if (problemId != null) criteria.add(" p.problem = :problem");
        if (username != null) criteria.add(" p.user = :user");
        if (category != null) criteria.add(" p.category = :category");
        if (!criteria.isEmpty()) jpql.append(" where ");

        for (int i = 0; i < criteria.size(); i++) {
            if (i > 0) jpql.append(" and ");
            jpql.append(criteria.get(i));
        }

        return jpql;
    }

    private void queryParameterBinding(Category category, User user, TypedQuery<Post> query, Problem problem) {
        if (user != null) query.setParameter("user", user);
        if (problem != null) query.setParameter("problem", problem);
        if (category != null) query.setParameter("category", category);
    }

    private Post checkPermission(Long postId, HttpServletRequest httpServletRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));

        Long userId = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (post.getUser() != user) throw new ApplicationException(NOT_PERMISSION);
        return post;
    }

    private void postTitleValidation(String title) {
        final int maxLength = 100;

        if (title.length() > maxLength) throw new ApplicationException(INVALID_TITLE_LEN);
    }
}
