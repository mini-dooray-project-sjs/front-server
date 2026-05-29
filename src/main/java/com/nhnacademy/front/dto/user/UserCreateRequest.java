package com.nhnacademy.front.dto.user;

import lombok.Builder;

@Builder
public record UserCreateRequest(
        String userId,
        String email,
        String password,
        UserRole role
) {
}
