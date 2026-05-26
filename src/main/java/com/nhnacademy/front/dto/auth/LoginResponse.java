package com.nhnacademy.front.dto.auth;

import lombok.Builder;

@Builder
public record LoginResponse(
        String userId,
        UserRole role
) {
}
