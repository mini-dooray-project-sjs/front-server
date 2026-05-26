package com.nhnacademy.front.dto.auth;

public record LoginUserResponse(
        String userId,
        UserRole role
) {
}
