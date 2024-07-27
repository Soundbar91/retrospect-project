package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestLoginUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestPasswordChange;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import com.soundbar91.retrospect_project.entity.Comment;
import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.CommentRepository;
import com.soundbar91.retrospect_project.repository.PostRepository;
import com.soundbar91.retrospect_project.repository.ProblemRepository;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_MATCH_PASSWORD;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(RequestCreateUser requestCreateUser) {
        String password = passwordEncoder.encode(requestCreateUser.password());
        userRepository.save(requestCreateUser.toEntity(password));
    }

    @Transactional
    public void changePassword(
            RequestPasswordChange requestPasswordChange,
            HttpServletRequest httpServletRequest
    ) {
        Long id = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        String newPassword = passwordEncoder.encode(requestPasswordChange.password());

        user.changePassword(newPassword);
        userRepository.flush();
    }

    @Transactional(readOnly = true)
    public ResponseUser findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        return ResponseUser.from(user);
    }

    @Transactional
    public void loginUser(RequestLoginUser requestLoginUser, HttpServletRequest request) {
        User user = userRepository.findByUsername(requestLoginUser.username())
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (!passwordEncoder.matches(requestLoginUser.password(), user.getPassword()))
            throw new ApplicationException(NOT_MATCH_PASSWORD);

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(3600);
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
    }

    @Transactional
    public void withdrawalUser(HttpServletRequest httpServletRequest) {
        Long id = (Long) httpServletRequest.getSession().getAttribute("userId");

        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        List<Problem> problemList = problemRepository.findProblemByUser(user);
        for (Problem problem : problemList) problem.deleteUser();

        List<Post> postList = postRepository.findPostByUser(user);
        for (Post post : postList) post.deleteUser();

        List<Comment> commentList = commentRepository.findCommentByUser(user);
        for (Comment comment : commentList) comment.deleteUser();

        userRepository.delete(user);
    }

}
