package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.AuthClient;
import com.nhnacademy.front.dto.auth.LoginRequest;
import com.nhnacademy.front.dto.auth.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthClient authClient;

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String login(
            @CookieValue(name="SESSION", required = false) String sessionId
    ) {
        // 세션 쿠키가 존재하면 로그인된 상태로 간주하여 프로젝트 페이지로 리다이렉트
        if(sessionId!=null && !sessionId.isEmpty()) {
            return "redirect:/projects";
        }

        // 세션 쿠키가 없으면 로그인 페이지로 이동
        return "auth/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String doLogin(
            @ModelAttribute LoginRequest request,
            HttpServletResponse response
    ) {
        try {
            ResponseEntity<LoginResponse> resp=authClient.login(request);

            List<String> cookies=resp.getHeaders().get(HttpHeaders.SET_COOKIE);
            if(cookies!=null) {
                for(String cookie: cookies) {
                    response.addHeader(HttpHeaders.SET_COOKIE, cookie);
                }
            }

            return "redirect:projects";
        } catch(Exception e) {
            return "redirect:/login?error";
        }
    }

    @PostMapping("/logout")
    public String doLogout(
            HttpServletResponse response
    ) {
        authClient.logout();

        ResponseCookie rc=ResponseCookie.from("SESSION", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,rc.toString());

        return "redirect:/login";
    }
}
