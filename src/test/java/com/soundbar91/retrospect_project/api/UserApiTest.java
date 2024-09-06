package com.soundbar91.retrospect_project.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.soundbar91.retrospect_project.fixture.UserFixture;
import com.soundbar91.retrospect_project.user.entity.User;
import com.soundbar91.retrospect_project.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiTest {

    private User user;
    private Long userId;
    private String username;
    private String password;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFixture userFixture;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void 초기_설정() throws Exception {
        user = userFixture.사용자();
        username = user.getUsername();
        password = "1q2w3e4r1!";

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                    .content("""
                        {
                          "username" : "%s",
                          "password" : "%s"
                        }
                        """.formatted(username, password))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(request().sessionAttribute("username", username))
            .andReturn();

        userId = (Long)result.getRequest().getSession().getAttribute("userId");
    }

    @Test
    void 비밀번호_변경() throws Exception {
        String newPassword = "1q2w3e4r4$";

        mockMvc.perform(
                put("/user/password")
                    .content("""
                        {
                          "curPassword" : "%s",
                          "newPassword" : "%s"
                        }
                        """.formatted(password, newPassword))
                    .contentType(MediaType.APPLICATION_JSON)
                    .sessionAttr("userId", userId)
                    .sessionAttr("username", username)
            )
            .andExpect(status().isOk());

        password = newPassword;
    }

    @Test
    void 유저_조회() throws Exception {
        mockMvc.perform(
                get("/user/" + username)
                    .contentType(MediaType.APPLICATION_JSON)
                    .sessionAttr("userId", userId)
                    .sessionAttr("username", username)
            )
            .andExpect(status().isOk())
            .andExpect(content().json("""
                {
                    "username": "testUser",
                    "email": "testUser@gmail.com",
                    "role": "브론즈",
                    "exp": 0.0
                }
                """));
    }

    @AfterAll
    void 회원탈퇴_종료() throws Exception {
        mockMvc.perform(
                delete("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .sessionAttr("userId", userId)
                    .sessionAttr("username", username)
            )
            .andExpect(status().isNoContent())
            .andExpect(request().sessionAttributeDoesNotExist("userId"))
            .andExpect(request().sessionAttributeDoesNotExist("username"));

        mockMvc.perform(
                get("/user/" + username)
                    .contentType(MediaType.APPLICATION_JSON)
                    .sessionAttr("userId", userId)
                    .sessionAttr("username", username)
            )
            .andExpect(status().isNotFound())
            .andExpect(content().json("""
                {
                    "message": "탈퇴한 유저입니다"
                }
                """));

        userRepository.deleteById(userId);
    }
}
