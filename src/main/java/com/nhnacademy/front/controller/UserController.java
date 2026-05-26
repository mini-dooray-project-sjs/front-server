package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.UserClient;
import com.nhnacademy.front.dto.user.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    // 회원가입 페이지로 이동
    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute UserCreateRequest req
    ) {
        userClient.register(req);

        return "redirect:/login";
    }
}
