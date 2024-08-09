package com.soundbar91.retrospect_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestLoginUser;
import com.soundbar91.retrospect_project.repository.UserRepository;
import com.soundbar91.retrospect_project.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthController authController;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인")
    void 로그인() throws Exception {
        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );
        RequestLoginUser requestLoginUser = new RequestLoginUser(
                "testUser", "1q2w3e4r1!"
        );

        // when
        ResultActions joinUserAction = mockMvc.perform(post("/user")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );
        ResultActions loginAction = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestLoginUser)));

        // then
        MvcResult mvcResult = loginAction.andReturn();
        HttpSession session = mvcResult.getRequest().getSession();

        assertNotNull(session.getAttribute("userId"));
        printResponseDetails(mvcResult);
    };

    @Test
    @DisplayName("로그인_실패_아이디틀림")
    void 로그인_실패_아이디틀림() throws Exception {
        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );
        RequestLoginUser requestLoginUser = new RequestLoginUser(
                "testser", "1q2w3e4r1!"
        );

        // when
        ResultActions joinUserAction = mockMvc.perform(post("/user")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );
        ResultActions loginAction = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestLoginUser)));

        // then
        MvcResult mvcResult = loginAction.andExpect(status().isNotFound()).andReturn();
        printResponseDetails(mvcResult);
    };

    @Test
    @DisplayName("로그인_실패_비밀번호틀림")
    void 로그인_실패_비밀번호틀림() throws Exception {
        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );
        RequestLoginUser requestLoginUser = new RequestLoginUser(
                "testUser", "1q2w3e4r5t1!"
        );

        // when
        ResultActions joinUserAction = mockMvc.perform(post("/user")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );
        ResultActions loginAction = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestLoginUser)));

        // then
        MvcResult mvcResult = loginAction.andExpect(status().isBadRequest()).andReturn();
        printResponseDetails(mvcResult);
    };

    @Test
    @DisplayName("로그아웃")
    void 로그아웃() throws Exception {
        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );
        RequestLoginUser requestLoginUser = new RequestLoginUser(
                "testUser", "1q2w3e4r1!"
        );

        // when
        mockMvc.perform(post("/user")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestCreateUser)))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestLoginUser))).andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertNotNull(session);

        ResultActions logoutAction = mockMvc.perform(post("/auth/logout").session(session));

        // then
        MvcResult logoutResult = logoutAction.andExpect(status().isNoContent()).andReturn();
        printResponseDetails(logoutResult);
    }

    private void printResponseDetails(MvcResult mvcResult) throws UnsupportedEncodingException {
        MockHttpServletResponse response = mvcResult.getResponse();
        System.out.println("MockHttpServletResponse:");
        System.out.println("           Status = " + response.getStatus());
        System.out.println("    Error message = " + response.getErrorMessage());
        System.out.println("         Headers = " + response.getHeaderNames().stream()
                .map(headerName -> headerName + ":" + response.getHeader(headerName))
                .reduce((header1, header2) -> header1 + ", " + header2)
                .orElse("No headers"));
        System.out.println("    Content type = " + response.getContentType());
        System.out.println("             Body = " + response.getContentAsString());
        System.out.println(" Forwarded URL = " + response.getForwardedUrl());
        System.out.println("Redirected URL = " + response.getRedirectedUrl());
        System.out.println("         Cookies = " + Arrays.toString(response.getCookies()));
    }
}
