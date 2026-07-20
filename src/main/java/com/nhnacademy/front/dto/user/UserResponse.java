package com.nhnacademy.front.dto.user;

import lombok.Builder;

@Builder
public record UserResponse(
        String userId,
        String email,
        UserStatus status,
        UserRole role
) {
}
