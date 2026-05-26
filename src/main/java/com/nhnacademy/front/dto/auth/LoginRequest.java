package com.nhnacademy.front.dto.auth;


public record LoginRequest(
        String userId,
        String password
) {
}
