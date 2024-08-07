package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import com.soundbar91.retrospect_project.controller.dto.response.ResponseUser;
import com.soundbar91.retrospect_project.service.ProblemService;
import com.soundbar91.retrospect_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;
    private final ProblemService problemService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/auth/join")
    public String register() {
        return "join";
    }

    @GetMapping("/main")
    public String problem(HttpServletRequest httpServletRequest, Model model) {
        String username = (String) httpServletRequest.getSession().getAttribute("username");
        model.addAttribute("username", username);
        return "main";
    }

    @GetMapping("/problem/search")
    public String problemSearch() {
        return "problemSearch";
    }

    @GetMapping("/problem/detail/{problemId}")
    public String problemDetail(
            @PathVariable("problemId") Long problemId,
            Model model
    ) {
        ResponseProblem problem = problemService.getProblem(problemId);
        model.addAttribute("problem", problem);
        return "problem";
    }

    @GetMapping("/submit/{problemId}")
    public String submit(@PathVariable(value = "problemId") Long problemId, Model model) {
        model.addAttribute("problemId", problemId);
        return "submit";
    }

    @GetMapping("/problem/maker")
    public String editor() {
        return "maker";
    }

    @GetMapping("/mypage")
    public String myPage(HttpServletRequest httpServletRequest, Model model) {
        String username = (String) httpServletRequest.getSession().getAttribute("username");
        ResponseUser user = userService.getUser(username);
        List<ResponseProblem> problems = problemService.getProblemsByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("problems", problems);
        return "myPage";
    }

    @GetMapping("/problem/{problemId}/editor")
    public String editor(
            @PathVariable(value = "problemId") Long problemId,
            HttpServletRequest httpServletRequest,
            Model model
    ) {
        ResponseProblem problem = problemService.getProblem(problemId);
        List<Map<String, Object>> testcase = problemService.getTestcase(problemId, httpServletRequest);

        model.addAttribute("problem", problem);
        model.addAttribute("testcase", testcase);
        return "editor";
    }

}
