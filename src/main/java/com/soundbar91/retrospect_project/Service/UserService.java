package com.soundbar91.retrospect_project.Service;

import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.repository.UserRepository;
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
}
