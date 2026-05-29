package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.auth.LoginRequest;
import com.nhnacademy.front.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${api.auth.url}")
    private String authUrl;

    // 로그인 처리
    public ResponseEntity<TokenResponse> login(LoginRequest req) {
        try{
            return restTemplate.postForEntity(
                    UriComponentsBuilder.fromUriString(authUrl)
                            .path("/login")
                            .toUriString(),
                    req,
                    TokenResponse.class);
        } catch(Exception e) {
            log.error("Login failed: {}", e.getMessage());
            throw e;
        }
    }

    // 로그아웃 처리
    public void logout() {
        try {
            restTemplate.postForEntity(
                    UriComponentsBuilder.fromUriString(authUrl)
                            .path("/logout")
                            .toUriString(),
                    null,
                    Void.class
            );
        } catch (Exception e) {
            log.warn("Logout failed: {}", e.getMessage());
        }
    }

    // 토큰 재발급 처리
    public ResponseEntity<TokenResponse> reissueToken(String refreshToken) {
        HttpHeaders headers=new HttpHeaders();
        headers.add("Cookie", "refreshToken="+refreshToken);
        HttpEntity<Void> entity=new HttpEntity<>(headers);

        return restTemplate.exchange(
                UriComponentsBuilder.fromUriString(authUrl)
                        .path("/refresh")
                        .toUriString(),
                HttpMethod.POST,
                entity,
                TokenResponse.class
        );
    }
}
