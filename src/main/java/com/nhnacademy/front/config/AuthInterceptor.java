package com.nhnacademy.front.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * accessToken 쿠키가 존재하는지 확인하는 인터셉터
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest req,
            HttpServletResponse resp,
            Object handler
    ) throws Exception {
        // 모든 요청에서 accessToken 쿠키가 존재하는지 확인
        Cookie[] cookies=req.getCookies();
        boolean hasAccessToken=false;
        boolean hasRefreshToken=false;

        if(cookies==null) {
            log.info("쿠키가 존재하지 않음. 로그인 페이지로 리다이렉트.");
            resp.sendRedirect("/login");
            return false; // 요청 처리 중단
        }

        // accessToken 쿠키가 존재하는지 확인
        for(Cookie cookie: cookies) {
            if("accessToken".equals(cookie.getName()) && cookie.getValue()!=null && !cookie.getValue().isEmpty()) {
                hasAccessToken=true;
                break;
            }

            if("refreshToken".equals(cookie.getName()) && cookie.getValue()!=null && !cookie.getValue().isEmpty()) {
                hasRefreshToken=true;
            }
        }

        // accessToken 쿠키가 존재하지 않거나 값이 비어있으면 로그인 페이지로 리다이렉트
        if(!hasAccessToken && !hasRefreshToken) {
            log.info("accessToken과 refreshToken이 모두 존재하지 않음. 로그인 페이지로 리다이렉트.");
            resp.sendRedirect("/login");
            return false; // 요청 처리 중단
        }

        return true; // 요청 처리 계속
    }
}
