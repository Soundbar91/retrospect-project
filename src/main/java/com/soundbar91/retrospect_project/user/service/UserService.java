package com.soundbar91.retrospect_project.user.service;

import com.soundbar91.retrospect_project.global.exception.ApplicationException;
import com.soundbar91.retrospect_project.user.controller.dto.RequestCreateUser;
import com.soundbar91.retrospect_project.user.controller.dto.RequestPasswordChange;
import com.soundbar91.retrospect_project.user.controller.dto.ResponseUser;
import com.soundbar91.retrospect_project.user.model.User;
import com.soundbar91.retrospect_project.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soundbar91.retrospect_project.global.exception.errorCode.AuthErrorCode.*;
import static com.soundbar91.retrospect_project.global.exception.errorCode.UserErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(RequestCreateUser requestCreateUser) {
        createUserValidation(requestCreateUser);

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
        if (!passwordEncoder.matches(requestPasswordChange.curPassword(), user.getPassword()))
            throw new ApplicationException(NOT_MATCH_PASSWORD);

        passwordValidation(requestPasswordChange.newPassword());

        String newPassword = passwordEncoder.encode(requestPasswordChange.newPassword());
        user.changePassword(newPassword);
        userRepository.flush();
    }

    public ResponseUser getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (!user.isActive()) throw new ApplicationException(WITHDREW_USER);

        return ResponseUser.from(user);
    }

    @Transactional
    public void deleteUser(HttpServletRequest httpServletRequest) {
        Long id = (Long) httpServletRequest.getSession().getAttribute("userId");
        userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER))
                .deactivate();

        userRepository.flush();
    }

    private void createUserValidation(RequestCreateUser requestCreateUser) {
        checkUserDuplicate(requestCreateUser);
        usernameValidation(requestCreateUser.username());
        emailValidation(requestCreateUser.email());
        passwordValidation(requestCreateUser.password());
    }

    private void checkUserDuplicate(RequestCreateUser requestCreateUser) {
        userRepository.findByUsername(requestCreateUser.username())
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_USERNAME);});

        userRepository.findByEmail(requestCreateUser.email())
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_EMAIL);});
    }

    private void usernameValidation(String username) {
        final String regex = "^[a-zA-Z0-9]+$";
        final int minLength = 6, maxLength = 20;

        if (username.length() < minLength || username.length() > maxLength)
            throw new ApplicationException(INVALID_USERNAME_LEN);

        if (!username.matches(regex))
            throw new ApplicationException(INVALID_USERNAME_PATTERN);
    }

    private void emailValidation(String email) {
        final String regex = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]+$";

        if (!email.matches(regex))
            throw new ApplicationException(INVALID_EMAIL_PATTERN);
    }

    private void passwordValidation(String password) {
        final String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.+[!@#$%^&*()/]).+$";
        final int minLength = 8, maxLength = 20;

        if (password.length() < minLength || password.length() > maxLength)
            throw new ApplicationException(INVALID_PASSWORD_LEN);

        if (!password.matches(regex))
            throw new ApplicationException(INVALID_PASSWORD_PATTERN);
    }

}
