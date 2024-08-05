package com.soundbar91.retrospect_project.controller;

import com.soundbar91.retrospect_project.controller.dto.response.ResponseProblem;
import com.soundbar91.retrospect_project.service.ProblemService;
import com.soundbar91.retrospect_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

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

    @GetMapping("/problemList")
    public String problemList() {
        return "problemList";
    }

    @GetMapping("/problem/detail/{problemId}")
    public String problemDetail(
            @PathVariable("problemId") Long problemId,
            Model model
    ) throws IOException {
        ResponseProblem problem = problemService.getProblem(problemId);
        model.addAttribute("problem", problem);
        return "problem";
    }

    @GetMapping("/submit/{problemId}")
    public String submit(@PathVariable(value = "problemId") Long problemId, Model model) {
        model.addAttribute("problemId", problemId);
        return "submit";
    }

    @GetMapping("/editor")
    public String editor() {
        return "editor";
    }

}
