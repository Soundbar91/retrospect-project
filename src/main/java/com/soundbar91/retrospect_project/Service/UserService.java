package com.soundbar91.retrospect_project.Service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestLoginUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestPasswordChange;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseUser createUser(RequestCreateUser requestCreateUser) {
        String password = passwordEncoder.encode(requestCreateUser.password());
        User user = userRepository.save(requestCreateUser.toEntity(password));

        return ResponseUser.from(user);
    }

    @Transactional
    public void changePassword(Long id, RequestPasswordChange requestPasswordChange) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        String newPassword = passwordEncoder.encode(requestPasswordChange.password());

        user.changePassword(newPassword);
        userRepository.flush();
    }

    @Transactional
    public void loginUser(RequestLoginUser requestLoginUser, HttpServletRequest request) {
        User user = userRepository.getByUsername(requestLoginUser.username());
        if (user == null) System.out.println("회원가입되지 않은 이메일입니다. ");

        boolean matches = passwordEncoder.matches(requestLoginUser.password(), user.getPassword());
        if (!matches) System.out.println("비밀번호가 틀렸습니다. ");

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
    public void withdrawalUser(Long id) {
        userRepository.deleteById(id);
    }
}
