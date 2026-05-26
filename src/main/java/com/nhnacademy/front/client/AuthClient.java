package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.auth.LoginRequest;
import com.nhnacademy.front.dto.auth.LoginResponse;
import com.nhnacademy.front.dto.auth.LoginUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${api.account.url}")
    private String accountUrl;

    // 로그인 처리
    public ResponseEntity<LoginResponse> login(LoginRequest req) {
        try{
            return restTemplate.postForEntity(
                    UriComponentsBuilder.fromUriString(accountUrl)
                            .path("/login")
                            .toUriString(),
                    req,
                    LoginResponse.class);
        } catch(Exception e) {
            log.error("Login failed: {}", e.getMessage());
            throw e;
        }
    }

    // 로그인된 사용자 정보 조회
    public LoginUserResponse getLoginUser() {
        try {
            return restTemplate.getForObject(
                    UriComponentsBuilder.fromUriString(accountUrl)
                            .path("/me")
                            .toUriString(),
                    LoginUserResponse.class
            );
        } catch(Exception e) {
            log.error("Failed to get login user: {}", e.getMessage());
            throw e;
        }
    }

    // 로그아웃 처리
    public void logout() {
        try {
            restTemplate.postForEntity(
                    UriComponentsBuilder.fromUriString(accountUrl).path("/logout").toUriString(),
                    null,
                    Void.class
            );
        } catch (Exception e) {
            log.warn("Logout failed: {}", e.getMessage());
        }
    }
}
