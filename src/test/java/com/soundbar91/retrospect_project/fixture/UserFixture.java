package com.soundbar91.retrospect_project.fixture;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.soundbar91.retrospect_project.user.entity.User;
import com.soundbar91.retrospect_project.user.repository.UserRepository;

@Component
@SuppressWarnings("NonAsciiCharacters")
public class UserFixture {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserFixture(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User 사용자() {
        return userRepository.save(
            User.builder()
                .username("testUser")
                .email("testUser@gmail.com")
                .password(passwordEncoder.encode("1q2w3e4r1!"))
                .isActive(true)
                .build()
        );
    }
}
