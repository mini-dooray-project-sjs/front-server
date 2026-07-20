package com.nhnacademy.front.controller;

import com.nhnacademy.front.client.AuthClient;
import com.nhnacademy.front.dto.auth.LoginRequest;
import com.nhnacademy.front.dto.auth.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthClient authClient;

    // 루트 경로로 접근 시 프로젝트 페이지로 리다이렉트
    @GetMapping("/")
    public String home() {
        return "redirect:/projects";
    }

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String login(
            @CookieValue(value="accessToken", required = false) String accessToken
    ) {
        // 세션 쿠키가 존재하면 로그인된 상태로 간주하여 프로젝트 페이지로 리다이렉트
        if(accessToken!=null && !accessToken.isEmpty()) {
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
            ResponseEntity<TokenResponse> resp=authClient.login(request);

            // 로그인 성공 시, API 서버에서 전달된 Set-Cookie 헤더를 그대로 클라이언트로 전달하여 세션 쿠키 설정
            List<String> cookies=resp.getHeaders().get(HttpHeaders.SET_COOKIE);
            if(cookies!=null) {
                for(String cookie: cookies) {
                    response.addHeader(HttpHeaders.SET_COOKIE, cookie);
                }
            }

            // API 서버에서 전달된 액세스 토큰을 클라이언트에서도 사용할 수 있도록 별도의 쿠키로 설정
            String accessToken=Objects.requireNonNull(resp.getBody()).accessToken();
            ResponseCookie accessTokenCookie=ResponseCookie.from("accessToken", accessToken)
                    .path("/")
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .maxAge(60*60)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

            return "redirect:/projects";
        } catch(Exception e) {
            return "redirect:/login?error";
        }
    }

    @PostMapping("/logout")
    public String doLogout(
            HttpServletResponse response
    ) {
        authClient.logout();

        ResponseCookie ac=ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,ac.toString());

        ResponseCookie rc=ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, rc.toString());

        return "redirect:/login";
    }
}
