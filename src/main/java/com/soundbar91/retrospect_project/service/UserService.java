package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestPasswordChange;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import com.soundbar91.retrospect_project.entity.*;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.soundbar91.retrospect_project.exception.errorCode.AuthErrorCode.DUPLICATE_EMAIL;
import static com.soundbar91.retrospect_project.exception.errorCode.AuthErrorCode.DUPLICATE_USERNAME;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ResultRepository resultRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(RequestCreateUser requestCreateUser) {
        checkUserDuplicate(requestCreateUser);

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

    public ResponseUser getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        return ResponseUser.from(user);
    }

    @Transactional
    public void deleteUser(HttpServletRequest httpServletRequest) {
        Long id = (Long) httpServletRequest.getSession().getAttribute("userId");
        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        removeUserAssociations(user);
        userRepository.delete(user);
    }

    private void checkUserDuplicate(RequestCreateUser requestCreateUser) {
        userRepository.findByUsername(requestCreateUser.username())
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_USERNAME);});

        userRepository.findByEmail(requestCreateUser.email())
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_EMAIL);});
    }

    private void removeUserAssociations(User user) {
        List<Problem> problemList = problemRepository.getByUser(user);
        for (Problem problem : problemList) problem.deleteUser();

        List<Post> postList = postRepository.getByUser(user);
        for (Post post : postList) post.deleteUser();

        List<Comment> commentList = commentRepository.getByUser(user);
        for (Comment comment : commentList) comment.deleteUser();

        List<Result> resultList = resultRepository.getByUser(user);
        for (Result result : resultList) result.deleteUser();
    }

}
