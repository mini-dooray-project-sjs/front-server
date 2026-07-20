package com.nhnacademy.front.client;

import com.nhnacademy.front.dto.user.UserCreateRequest;
import com.nhnacademy.front.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @Value("${api.account.url}")
    private String accountUrl;

    // 회원가입
    public UserResponse register(UserCreateRequest req) {
        return restTemplate.postForObject(
                accountUrl + "/users",
                req,
                UserResponse.class
        );
    }
}
