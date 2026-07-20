package com.nhnacademy.front.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 인터셉터 등록 및 설정을 담당
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(
            InterceptorRegistry registry
    ) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns(
                        "/login", "/logout",    // 로그인과 로그아웃 경로는 인터셉터 적용 제외
                        "/users/register",      // 회원가입 경로는 인터셉터 적용 제외
                        "/css/**", "/js/**", "/images/**",  // 정적 리소스 경로는 인터셉터 적용 제외
                        "/favicon.ico",         // 파비콘 경로는 인터셉터 적용 제외
                        "/error"                // 에러 페이지 경로는 인터셉터 적용 제외
                );
    }
}
