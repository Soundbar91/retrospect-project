package com.soundbar91.retrospect_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soundbar91.retrospect_project.controller.dto.request.RequestCreateUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestLoginUser;
import com.soundbar91.retrospect_project.controller.dto.request.RequestPasswordChange;
import com.soundbar91.retrospect_project.exception.GlobalControllerAdvice;
import com.soundbar91.retrospect_project.repository.UserRepository;
import com.soundbar91.retrospect_project.service.AuthService;
import com.soundbar91.retrospect_project.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

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
    @DisplayName("회원가입_정상")
    void 회원가입_정상() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_아이디조건_미충족_짧음")
    void 회원가입_아이디조건_미충족_짧음() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "test",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디는 최소 6자리, 최대 20자리입니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_아이디조건_미충족_긺")
    void 회원가입_아이디조건_미충족_긺() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testtesttesttesttesttesttesttesttesttesttesttesttesttesttest",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디는 최소 6자리, 최대 20자리입니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_아이디조건_미충족_한글")
    void 회원가입_아이디조건_미충족_한글() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "테스트계정입니다",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디는 영어와 숫자로만 구성되야합니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_비밀번호조건_짧음")
    void 회원가입_비밀번호조건_짧음() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 최소 8자리, 최대 20자리입니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_비밀번호조건_긺")
    void 회원가입_비밀번호조건_긺() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r5t6y7u8i9o0p!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 최소 8자리, 최대 20자리입니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_비밀번호조건_특수문자미포함")
    void 회원가입_비밀번호조건_특수문자미포함() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r5t"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 영어, 숫자 그리고 특수문자로 구성되며, !@#$%^&*()/ 중 하나 이상을 포함해야 합니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_비밀번호조건_영어만포함")
    void 회원가입_비밀번호조건_영어만포함() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "qwerasdf"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 영어, 숫자 그리고 특수문자로 구성되며, !@#$%^&*()/ 중 하나 이상을 포함해야 합니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_이메일조건_골뱅이미포함")
    void 회원가입_이메일조건_골뱅이미포함() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUsernaver.com",
                "1q2w3e4r1!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일은 user@example.com와 같은 형식으로 입력해야합니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("회원가입_이메일조건_콤마미포함")
    void 회원가입_이메일조건_콤마미포함() throws Exception {

        // given
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@navercom",
                "1q2w3e4r1!"
        );

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일은 user@example.com와 같은 형식으로 입력해야합니다."))
                .andReturn();

        printResponseDetails(mvcResult);
    }

    @Test
    @DisplayName("비밀번호_변경")
    void 비밀번호_변경() throws Exception {
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );
        RequestLoginUser requestLoginUser = new RequestLoginUser(
                "testUser", "1q2w3e4r1!"
        );
        RequestPasswordChange requestPasswordChange = new RequestPasswordChange(
                "1q2w3e4r1!", "1q2w3e4r4$"
        );

        // when
        ResultActions joinUserAction = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestLoginUser)))
                .andReturn().getRequest().getSession();
        ResultActions changePasswordAction = mockMvc.perform(put("/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestPasswordChange))
                .session(session)
        );

        // then
        MvcResult changePasswordResult = changePasswordAction
                .andExpect(status().isOk())
                .andReturn();

        printResponseDetails(changePasswordResult);
    }

    @Test
    @DisplayName("비밀번호_변경_실패_현재비밀번호미일치")
    void 비밀번호_변경_실패_현재비밀번호미일치() throws Exception {
        RequestCreateUser requestCreateUser = new RequestCreateUser(
                "testUser",
                "testUser@naver.com",
                "1q2w3e4r1!"
        );
        RequestLoginUser requestLoginUser = new RequestLoginUser(
                "testUser", "1q2w3e4r1!"
        );
        RequestPasswordChange requestPasswordChange = new RequestPasswordChange(
                "1q2w3e4r11!", "1q2w3e4r4$"
        );

        // when
        ResultActions joinUserAction = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCreateUser))
        );
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestLoginUser)))
                .andReturn().getRequest().getSession();
        ResultActions changePasswordAction = mockMvc.perform(put("/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestPasswordChange))
                .session(session)
        );

        // then
        MvcResult changePasswordResult = changePasswordAction
                .andExpect(status().isBadRequest())
                .andReturn();

        printResponseDetails(changePasswordResult);
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
