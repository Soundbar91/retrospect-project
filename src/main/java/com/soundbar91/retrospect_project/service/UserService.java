package com.soundbar91.retrospect_project.service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestPasswordChange;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.exception.ApplicationException;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soundbar91.retrospect_project.exception.errorCode.AuthErrorCode.*;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.NOT_FOUND_USER;
import static com.soundbar91.retrospect_project.exception.errorCode.UserErrorCode.WITHDREW_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
        if (!passwordEncoder.matches(requestPasswordChange.curPassword(), user.getPassword()))
            throw new ApplicationException(NOT_MATCH_PASSWORD);

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

    private void checkUserDuplicate(RequestCreateUser requestCreateUser) {
        userRepository.findByUsername(requestCreateUser.username())
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_USERNAME);});

        userRepository.findByEmail(requestCreateUser.email())
                .ifPresent(e -> {throw new ApplicationException(DUPLICATE_EMAIL);});
    }

}
