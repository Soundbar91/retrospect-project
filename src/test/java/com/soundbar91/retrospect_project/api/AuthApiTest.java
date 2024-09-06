package com.soundbar91.retrospect_project.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFixture userFixture;

    @Test
    void 회원_로그인() throws Exception {
        User user = userFixture.사용자();
        String username = user.getUsername();
        String password = "1q2w3e4r1!";

        mockMvc.perform(
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
            .andExpect(request().sessionAttribute("username", username));
    }

    @Test
    void 회원_로그아웃() throws Exception {
        User user = userFixture.사용자();
        String username = user.getUsername();
        String password = "1q2w3e4r1!";

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

        Long sessionUserid = (Long)result.getRequest().getSession().getAttribute("userId");
        String sessionUsername = (String)result.getRequest().getSession().getAttribute("username");

        mockMvc.perform(
                post("/auth/logout")
                    .sessionAttr("userId", sessionUserid)
                    .sessionAttr("username", sessionUsername)
            )
            .andExpect(status().isNoContent())
            .andExpect(request().sessionAttributeDoesNotExist("userId"))
            .andExpect(request().sessionAttributeDoesNotExist("username"));
    }
}
