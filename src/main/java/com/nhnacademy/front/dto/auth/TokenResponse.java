package com.nhnacademy.front.dto.auth;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
