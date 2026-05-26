package com.nhnacademy.front.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 통신중, 인증/인가 실패로 인해 401, 403 에러가 발생한 경우, 세션 쿠키를 제거하고 로그인 페이지로 리다이렉트
    @ExceptionHandler({
            HttpClientErrorException.Unauthorized.class,
            HttpClientErrorException.Forbidden.class
    })
    public String handlerHttpClientErrorException(
            HttpServletResponse resp
    ) {
        // 세션 쿠키 제거
        ResponseCookie rc=ResponseCookie.from("SESSION", "")
                .path("/")
                .maxAge(0)
                .build();
        resp.addHeader(HttpHeaders.SET_COOKIE, rc.toString());

        log.warn("401/403 error: 세션 쿠키 제거 후 로그인 페이지로 리다이렉트");

        // 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }

    // API 서버와의 통신 중 발생한 예외 처리
    @ExceptionHandler(HttpStatusCodeException.class)
    public String handleBackendApiException(
            HttpStatusCodeException ex,
            Model model
    ) {
        // API 서버에서 반환된 에러 메시지와 상태 코드를 로그에 기록
        String errorMessage=ex.getResponseBodyAsString();
        int statusCode=ex.getStatusCode().value();

        // API 서버에서 명확한 에러 메시지가 없는 경우, 기본 메시지 설정
        if(errorMessage==null || errorMessage.isEmpty()) {
            errorMessage="API서버 통신 중 오류가 발생했습니다.";
        }

        log.error("API error: status={}, message={}", statusCode, errorMessage);

        // 에러 메시지와 상태 코드를 모델에 추가
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);

        // 에러 페이지로 이동
        return "error/error";
    }

    // 브라우저 확장/자동 요청으로 인해 발생하는 정적 리소스 누락 예외 처리
    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFound(NoResourceFoundException ex, HttpServletResponse resp) {
        // 브라우저 확장/자동 요청으로 발생하는 정적 리소스 누락 -> 디버그로만 기록
        log.debug("NoResourceFoundException: {}", ex.getMessage());
        // 404 상태코드만 반환
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    // 그 외의 예외 처리 - 예기치 못한 오류에 대한 일반적인 에러 페이지로 이동
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(
            Exception ex,
            Model model
    ) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        // 예기치 못한 오류에 대한 기본 메시지와 상태 코드 설정
        model.addAttribute("statusCode", 500);
        model.addAttribute("errorMessage", "예기치 못한 오류가 발생했습니다.");

        // 에러 페이지로 이동
        return "error/error";
    }
}
